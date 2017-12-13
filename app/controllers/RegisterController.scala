package controllers

import java.util.UUID
import javax.inject.{Inject, Singleton}

import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasherRegistry
import com.mohiva.play.silhouette.api.{LoginInfo, SignUpEvent, SilhouetteProvider}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import models.{User, UserType}
import models.db.UserService
import play.api.data.Form
import play.api.data.Forms.{email, mapping, nonEmptyText, text}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.ws.WSClient
import play.api.libs.mailer.{Email, MailerClient}
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import services.UserData
import util.CookieEnv

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RegisterController @Inject()(
                                    silhouette: SilhouetteProvider[CookieEnv],
                                    userService: UserService,
                                    authInfoRepository: AuthInfoRepository,
                                    passwordHasherRegistry: PasswordHasherRegistry,
                                    mailerClient: MailerClient
                                  )(
                                    implicit ec: ExecutionContext,
                                    cc: ControllerComponents,
                                    messagesApi: MessagesApi,
                                    ws: WSClient) extends AbstractController(cc) with I18nSupport {

  val allowedUserTypes: Set[String] = UserType.values.map { _.toString }
  val formUserTypes = List(UserType.Business, UserType.Research).map { t => t.toString -> t.toString }

  val userForm = Form(
    mapping(
      "userType" -> text.verifying { allowedUserTypes.apply _ },
      "name" -> nonEmptyText,
      "country" -> nonEmptyText,
      "email" -> email,
      "login" -> nonEmptyText,
      "password" -> nonEmptyText
    )(UserData.apply)(UserData.unapply)
  )

  def form = silhouette.UnsecuredAction { implicit request: Request[AnyContent] =>
    Ok(views.html.register(userForm, formUserTypes))
  }

  def checkCaptcha(request: Request[AnyContent]): Future[Boolean] = {
    val formParams = request.body.asFormUrlEncoded.get
    val captchaResponse = formParams("g-recaptcha-response")
    val remoteAddress = request.remoteAddress
    val secretKey = "6LeGoTYUAAAAAET29aHT_Y6wONkmu9ssZDDLyR7T"
    val validationRequest = ws.url("https://www.google.com/recaptcha/api/siteverify")
    val params = Map(
      "secret" -> Seq(secretKey),
      "response" -> captchaResponse,
      "remoteid" -> Seq(remoteAddress)
    )
    val validationFuture = validationRequest.post(params)
    validationFuture.flatMap { validationResponse =>
      val result = validationResponse.json
      if ((result \ "success").as[Boolean]) Future.successful(true)
      else Future.failed(new SecurityException((result \ "error-codes").toString))
    }
  }

  def sendEmail() = Action {
    val url = "someurl"
    val email = "a@a.a"
    val login = "myLogin"
    val name = "myName"
    val country = "myCountry"
    val loginInfo = LoginInfo(CredentialsProvider.ID, login)
    val user = User(UUID.randomUUID(), loginInfo, name, country, email, UserType.Admin)
    mailerClient.send(Email(
      subject = "ScienceProvider Account Activation",
      from = "No-reply ScienceProvider",
      to = Seq(email),
      bodyText = Some(views.txt.emails.signUp(user, url).body),
      bodyHtml = Some(views.html.emails.signUp(user, url).body)
    ))
    Ok("Email sent!")
  }

  def userPost = silhouette.UnsecuredAction.async { implicit request: Request[AnyContent] =>
    userForm.bindFromRequest.fold(
      formWithErrors => Future(BadRequest("Failed")),
      contact => {
        val loginInfo = LoginInfo(CredentialsProvider.ID, contact.login)
        val infos = for {
          _ <- checkCaptcha(request)
          userInfo <- userService.retrieve(loginInfo)
        } yield userInfo
        val response = infos.flatMap {
          case Some(user) => Future(Ok("Hi " + contact.name + "! You are already registered!" + user.id))
          case None =>
            val authInfo = passwordHasherRegistry.current.hash(contact.password)
            val userType = UserType.withName(contact.userType)
            val user = User(UUID.randomUUID(), loginInfo, contact.name, contact.country, contact.email, userType)
            for {
              _ <- userService.save(user)
              _ <- authInfoRepository.add(loginInfo, authInfo)
              _ = silhouette.env.eventBus.publish(SignUpEvent(user, request))
            } yield {
              // TODO: send a confirmation email
              Redirect(routes.HomeController.index())
            }
        }
        response.recover {
          case ex: SecurityException =>
            BadRequest("Captcha not passed:\n" + ex)
          //Redirect(routes.LoginController.form()).flashing("error" -> "invalid.credentials")
        }
      }
    )

  }

}

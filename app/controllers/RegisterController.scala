package controllers

import java.util.UUID
import javax.inject.{Inject, Singleton}

import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasherRegistry
import com.mohiva.play.silhouette.api.{LoginInfo, SignUpEvent, Silhouette, SilhouetteProvider}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import models.{User, UserService}
import play.api.data.Form
import play.api.data.Forms.{email, mapping, nonEmptyText}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.ws.WSClient
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import services.UserData
import util.CookieEnv

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class RegisterController @Inject()(
                                    silhouette: SilhouetteProvider[CookieEnv],
                                    userService: UserService,
                                    authInfoRepository: AuthInfoRepository,
                                    passwordHasherRegistry: PasswordHasherRegistry
                                  )(
    implicit ec: ExecutionContext,
    cc: ControllerComponents,
    messagesApi: MessagesApi,
    ws: WSClient) extends AbstractController(cc) with I18nSupport {


  val userForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "country" -> nonEmptyText,
      "email" -> email,
      "login" -> nonEmptyText,
      "password" -> nonEmptyText
    )(UserData.apply)(UserData.unapply)
  )

  def form = Action { implicit request : Request[AnyContent] =>
    Ok(views.html.register(userForm))
  }

  def checkCaptcha(request : Request[AnyContent]): Future[Unit] = {
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
    validationFuture.map { validationResponse =>
      val result = validationResponse.json
      if ((result \ "success").as [Boolean]) Future.successful(Unit)
      else Future.failed(new SecurityException((result \ "error-codes").toString))
    }
  }

  def userPost = silhouette.UnsecuredAction.async { implicit request : Request[AnyContent] =>
    userForm.bindFromRequest.fold(
      formWithErrors => Future(BadRequest("Failed")),
      contact => {
        checkCaptcha(request).flatMap { _ =>
          val loginInfo = LoginInfo(CredentialsProvider.ID, contact.login)
          userService.retrieve(loginInfo).map {
            case Some(user) => Ok("Hi " + contact.name + "! " + user.id)
            case None =>
              val authInfo = passwordHasherRegistry.current.hash(contact.password)
              val user = User(
                UUID.randomUUID(),
                loginInfo,
                contact.name,
                contact.country,
                contact.email)
              userService.save(user)
              authInfoRepository.add(loginInfo, authInfo)
              silhouette.env.eventBus.publish(SignUpEvent(user, request))

              println(UserService.users)

              Ok("New user")
          }
        }
      }
    )

  }

}

package controllers

import javax.inject.{Inject, Singleton}

import com.mohiva.play.silhouette.api.SilhouetteProvider
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.Credentials
import com.mohiva.play.silhouette.impl.exceptions.IdentityNotFoundException
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import models.db.UserService
import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import services.LoginData
import util.CookieEnv

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class LoginController @Inject()(
                                 silhouette: SilhouetteProvider[CookieEnv],
                                 userService: UserService,
                                 credentialsProvider: CredentialsProvider,
                                 authInfoRepository: AuthInfoRepository,
                                 cc: ControllerComponents,
                                 messagesApi: MessagesApi)
                               (
                                 implicit ec: ExecutionContext
                               ) extends AbstractController(cc) with I18nSupport {

  val loginForm = Form(
    mapping(
      "login" -> nonEmptyText,
      "password" -> nonEmptyText
    )(LoginData.apply)(LoginData.unapply)
  )

  def login = silhouette.UnsecuredAction.async { implicit request: Request[AnyContent] =>
    loginForm.bindFromRequest.fold(
      formWithErrors => {
        println(formWithErrors)
        Future(BadRequest("Form errors"))
      }, // Future(BadRequest(views.html.loginform(loginForm))),
      contact => {
        val credentials = Credentials(contact.login, contact.password)
        credentialsProvider.authenticate(credentials).flatMap { loginInfo =>
          userService.retrieve(loginInfo).flatMap {
            case None => Future.failed(new IdentityNotFoundException("Couldn't find user"))
            case Some(user) =>
              for {
                authenticator <- silhouette.env.authenticatorService.create(loginInfo)
                cookie <- silhouette.env.authenticatorService.init(authenticator)
                result <- silhouette.env.authenticatorService.embed(cookie, Redirect(routes.HomeController.index()))
              } yield result
          }
        }.recover {
          case ex: ProviderException =>
            BadRequest("Invalid credentials:\n" + ex)
          //Redirect(routes.LoginController.form()).flashing("error" -> "invalid.credentials")
        }
      })
  }
}
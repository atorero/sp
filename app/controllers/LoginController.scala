package controllers

import javax.inject.{Inject, Singleton}

import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.Credentials
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import models.{User, UserService}
import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import services.LoginData

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class LoginController @Inject()(
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

  def form = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.loginform(loginForm))
  }

  def login = Action.async { implicit request: Request[AnyContent] =>
    loginForm.bindFromRequest.fold(
      formWithErrors => Future(BadRequest("Failed")),
      contact => {
        val credentials = Credentials(contact.login, contact.password)
        credentialsProvider.authenticate(credentials).map { loginInfo =>
          println(loginInfo)
          Ok("Logged in!")
        }
      })
  }
}
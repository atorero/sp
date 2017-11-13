package controllers

import javax.inject.{Inject, Singleton}

import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import services.LoginData

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class LoginController @Inject()(
    implicit ec: ExecutionContext,
    cc: ControllerComponents,
    messagesApi: MessagesApi) extends AbstractController(cc) with I18nSupport {

  val loginForm = Form(
    mapping(
      "login" -> nonEmptyText,
      "password" -> nonEmptyText
    )(LoginData.apply)(LoginData.unapply)
  )

  def form = Action { implicit request : Request[AnyContent] =>
    Ok(views.html.loginform(loginForm))
  }

  def login = Action.async { implicit request: Request[AnyContent] =>
    loginForm.bindFromRequest.fold(
      formWithErrors => Future(BadRequest("Failed")),
      contact => {
        Future(Ok("Logged in!"))
      })
  }
}
package controllers

import javax.inject._

import play.api.mvc._
import play.api.i18n._
import play.api.i18n.MessagesApi
import play.api.i18n.Messages.Implicits._
import play.api.data._
import play.api.data.Forms._

import services.UserData

@Singleton
class RegisterController @Inject()(
    cc: ControllerComponents,
    messagesApi: MessagesApi) extends AbstractController(cc) with I18nSupport {


  val userForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "country" -> nonEmptyText,
      "email" -> nonEmptyText,
      "login" -> nonEmptyText,
      "password" -> nonEmptyText
    )(UserData.apply)(UserData.unapply)
  )

  def form = Action { implicit request : Request[AnyContent] =>
    Ok(views.html.register(userForm))
  }

  def userPost = Action { implicit request : Request[AnyContent] =>
    userForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest("Failed")
      },
      contact => {
        val name = contact.name
        Ok("Hi " + name + "!")
      }
    )

  }

}

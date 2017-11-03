package controllers

import javax.inject._

import play.api.mvc._
import play.api.i18n._
import play.api.i18n.MessagesApi
import play.api.i18n.Messages.Implicits._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.ws.WSClient
import play.api.libs.json.JsBoolean
import scala.concurrent.{ExecutionContext, Future}

import services.UserData

@Singleton
class RegisterController @Inject()(
    implicit ec: ExecutionContext,
    cc: ControllerComponents,
    messagesApi: MessagesApi,
    ws: WSClient) extends AbstractController(cc) with I18nSupport {


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

  def userPost = Action.async { implicit request : Request[AnyContent] =>
    userForm.bindFromRequest.fold(
      formWithErrors => {
        Future(BadRequest("Failed"))
      },
      contact => {
        val captchaResponse = request.getQueryString("g-recaptcha-response").get
        val remoteAddress = request.remoteAddress
        println(remoteAddress)
        val secretKey = "6LeGoTYUAAAAAET29aHT_Y6wONkmu9ssZDDLyR7T"

        val validationRequest = ws.url("https://www.google.com/recaptcha/api/siteverify")

        val params = Map(
          "secret" -> Seq(secretKey),
          "response" -> Seq(captchaResponse),
          "remoteid" -> Seq(remoteAddress)
        )

        val validationFuture = validationRequest.post(params)
        validationFuture.map { validationResponse =>
          val name = contact.name
          val result = validationResponse.json
          println(result)
          if ((result \ "success").as [Boolean])
            Ok("Hi " + name + "!\n")
          else
            Ok("Captcha not passed: " + (result \ "error-codes"))
        }


      }
    )

  }

}

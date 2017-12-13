package controllers

import javax.inject.{Inject, Singleton}

import com.mohiva.play.silhouette.api.SilhouetteProvider
import models.db.{UserRecord, UserService}
import play.api.i18n.I18nSupport
import play.api.mvc._
import slick.jdbc.H2Profile.api._
import slick.lifted.TableQuery
import util.CookieEnv

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AdminController @Inject()(
                                silhouette: SilhouetteProvider[CookieEnv],
                                cc: ControllerComponents,
                                userService: UserService
                              )
                              (implicit ec: ExecutionContext)
  extends AbstractController(cc) with I18nSupport {

  def adminAction(f: /*SecuredRequest[CookieEnv, AnyContent]*/ => Future[Result]) = silhouette.SecuredAction.async { implicit request =>
    val user = request.identity
    if (user.loginInfo.providerKey != "torero") Future(BadRequest("Unauthorised user!"))
    else f
  }

  def createUsers = adminAction {
    userService.db.run(TableQuery[UserRecord].schema.create).map { _ => Ok("Users table created") }
  }

  /*
  def dropUsers = adminAction {
    userService.db.run(TableQuery[UserRecord].schema.drop).map { _ => Ok("Users table dropped") }
  }
  */

}

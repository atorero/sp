package controllers

import javax.inject.{Inject, Singleton}

import com.mohiva.play.silhouette.api.SilhouetteProvider
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import util.CookieEnv

@Singleton
class BackofficeController @Inject()(
                                      silhouette: SilhouetteProvider[CookieEnv],
                                      cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {

  def index = silhouette.SecuredAction { implicit request =>
    val user = request.identity
    Ok(views.html.backoffice_index(user))
  }

}

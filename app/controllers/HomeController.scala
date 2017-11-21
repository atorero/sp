package controllers

import javax.inject.{Inject, Singleton}

import com.mohiva.play.silhouette.api.SilhouetteProvider
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, ControllerComponents}
import util.CookieEnv

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(
                              silhouette: SilhouetteProvider[CookieEnv],
                              cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {

  def index = silhouette.UserAwareAction { implicit request =>
    Ok(views.html.index(request.identity))
  }

}

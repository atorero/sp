package controllers

import javax.inject.{Inject, Singleton}

import com.mohiva.play.silhouette.api.SilhouetteProvider
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, ControllerComponents}
import util.CookieEnv

/**
  * Created by amikhaylov8 on 10.10.17.
  */
@Singleton
class ScienceController @Inject()(
                                   silhouette: SilhouetteProvider[CookieEnv],
                                   cc: ControllerComponents)
  extends AbstractController(cc) with I18nSupport{

  def index = silhouette.UserAwareAction { implicit request =>
    Ok(views.html.scienceindex(request.identity))
  }

}

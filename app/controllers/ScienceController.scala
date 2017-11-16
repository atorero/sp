package controllers

import javax.inject.{Inject, Singleton}

import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, ControllerComponents}

/**
  * Created by amikhaylov8 on 10.10.17.
  */
@Singleton
class ScienceController @Inject()(cc: ControllerComponents)
  extends AbstractController(cc) with I18nSupport{

  def index = Action { implicit request =>
    Ok(views.html.scienceindex())
  }

}

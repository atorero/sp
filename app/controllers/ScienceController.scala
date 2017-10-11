package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{AbstractController, ControllerComponents}

/**
  * Created by amikhaylov8 on 10.10.17.
  */
@Singleton
class ScienceController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index = Action {
    Ok(views.html.scienceindex())
  }

}

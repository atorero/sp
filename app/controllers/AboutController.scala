package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{AbstractController, ControllerComponents}
import services.Lab

/**
  * Created by amikhaylov8 on 10.10.17.
  */
@Singleton
class AboutController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def about = Action {
    Ok(views.html.about())
  }

}

package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.mvc._
import services.Lab

/**
  * Created by amikhaylov8 on 10.10.17.
  */
@Singleton
class LabsController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def all: Seq[Lab] = {
    List(
      Lab("LPMV", "EPFL", "Switzerland"),
      Lab("Random", "MIPT", "Russia"))
  }

  def list = Action {
    Ok(views.html.labs(all))
  }

}

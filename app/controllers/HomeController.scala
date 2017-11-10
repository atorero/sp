package controllers

import javax.inject.{Singleton, Inject}

import play.api.mvc.{AbstractController, ControllerComponents}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index = Action {
    Ok(views.html.index())
  }

}

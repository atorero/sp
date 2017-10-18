package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.mvc._
import services.{Lab, LabsRecord}

/**
  * Created by amikhaylov8 on 10.10.17.
  */
@Singleton
class LabsController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  val dummy = List(
    Lab("LPMV", "EPFL", "Switzerland"),
    Lab("Bioinformatics", "MIPT", "Russia"))

  def all: Seq[Lab] = LabsRecord.getAll

  def list = Action {
    Ok(views.html.labs(all))
  }

  def createDB = Action {
    LabsRecord.create
    Ok("Created the LABS table!")
  }

  def dropTable = Action {
    LabsRecord.drop
    Ok("dropped the LABS table!")
  }

  def add(name: String, university: String, country: String) =  Action {
    val lab = Lab(name, university, country)
    LabsRecord.add(lab)
    Ok(lab.toString)
  }

}

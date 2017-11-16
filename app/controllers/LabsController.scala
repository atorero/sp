package controllers

import javax.inject.{Inject, Singleton}

import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, ControllerComponents}
import services.{Lab, LabsRecord}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by amikhaylov8 on 10.10.17.
  */
@Singleton
class LabsController @Inject()(implicit ec: ExecutionContext, cc: ControllerComponents)
  extends AbstractController(cc) with I18nSupport {

  val dummy = List(
    Lab("LPMV", "EPFL", "Switzerland"),
    Lab("Bioinformatics", "MIPT", "Russia"))

  def list = Action.async { implicit request =>
    LabsRecord.getAll.map { labs => Ok(views.html.labs(labs)) }
  }

  def get(id: Int) = Action.async { implicit request =>
    LabsRecord.get(id).map { lab => Ok(views.html.lab(lab)) }
  }

  def createDB = Action.async {
    LabsRecord.create.map {_ => Ok("Created the LABS table!") }
  }

  def dropTable = Action.async {
    LabsRecord.drop.map { _ => Ok("dropped the LABS table!") }
  }

  def add(name: String, university: String, country: String) =  Action.async {
    val lab = Lab(name, university, country)
    LabsRecord.add(lab).map { id => Ok(s"Inserted a new lab with id=$id") }
  }

  def addDummy = Action.async {
    val insertChain = dummy.foldLeft ( Future(-1) ) { (prev, lab) => prev.flatMap(_ => LabsRecord.add(lab)) }
    insertChain.map { _ => Ok(dummy.toString) }
  }

}

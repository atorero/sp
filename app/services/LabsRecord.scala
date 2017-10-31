package services

import services.Lab.LabTuple
import slick.jdbc.H2Profile.api._
import slick.lifted.{TableQuery, Tag}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by amikhaylov8 on 17.10.17.
  */
class LabsRecord(tag: Tag) extends Table[LabTuple](tag, "LABS") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def name = column[String]("NAME")
  def university = column[String]("UNIVERSITY")
  def country = column[String]("COUNTRY")

  def * = (id, name, university, country)
  def idx = index("NAME_UNIVERSITY", (name, university), unique = true)
}



object LabsRecord {

  val dbName = "h2file"
  val db = Database.forConfig(dbName)

  def create = db.run(TableQuery[LabsRecord].schema.create)

  def drop = db.run(TableQuery[LabsRecord].schema.drop)

  def add(lab: Lab): Future[Int] = {
    val labs = TableQuery[LabsRecord]
    db.run(labs += ( 0, lab.name, lab.university, lab.country  ))
  }

  def getAll: Future[Seq[Lab]] = {
    val labsQuery = TableQuery[LabsRecord]
    db.run(labsQuery.result)
      .map { _.map { Lab.apply } }
  }

  def get(id: Int): Future[Lab] = {
    val labs = TableQuery[LabsRecord].filter {
      _.id === id
    }
    db.run(labs.result)
      .map {
        case Seq(tuple: LabTuple) => Lab(tuple)
      }
  }

}

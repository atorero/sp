package models.db

import slick.jdbc.H2Profile.api._
import slick.lifted.ProvenShape.proveShapeOf
import slick.lifted.{TableQuery, Tag}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by amikhaylov8 on 17.10.17.
  */
class LabsRecord(tag: Tag) extends Table[Lab](tag, "LABS") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def name = column[String]("NAME")
  def university = column[String]("UNIVERSITY")
  def country = column[String]("COUNTRY")

  def * = (id, name, university, country) <> (Lab.tupled, Lab.unapply)
  def idx = index("NAME_UNIVERSITY", (name, university), unique = true)
}



object LabsRecord {

  val dbName = "h2file"
  val db = Database.forConfig(dbName)

  def create = db.run(TableQuery[LabsRecord].schema.create)

  def drop = db.run(TableQuery[LabsRecord].schema.drop)

  def add(lab: Lab): Future[Int] = db.run(TableQuery[LabsRecord] += Lab( 0, lab.name, lab.university, lab.country  ))

  def getAll: Future[Seq[Lab]] = db.run(TableQuery[LabsRecord].result)

  def get(id: Int): Future[Lab] = {
    val labs = TableQuery[LabsRecord].filter { _.id === id }
    db.run(labs.result).map { _.head }
  }

}

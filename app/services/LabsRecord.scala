package services

import slick.jdbc.H2Profile.api._
import slick.lifted.{TableQuery, Tag}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by amikhaylov8 on 17.10.17.
  */
class LabsRecord(tag: Tag) extends Table[(Int, String, String, String)](tag, "LABS") {
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

  def create = {
    db.run(TableQuery[LabsRecord].schema.create).onComplete {
      case Success(_) => println("Table created")
      case Failure(ex) => println(ex)
    }
  }

  def drop = {
    db.run(TableQuery[LabsRecord].schema.drop).onComplete {
      case Success(_) => println("Table dropped")
      case Failure(ex) => println(ex)
    }
  }

  def add(lab: Lab) = {
    val labs = TableQuery[LabsRecord]
    db.run(labs += ( 0, lab.name, lab.university, lab.country  )).onComplete {
      case Success(_) => println("Lab inserted")
      case Failure(ex) => println(ex)
    }
  }

  def getAll: Seq[Lab] = {
    val labs = TableQuery[LabsRecord]
    val retrieved = Await.result(db.run(labs.result), 1 second)
    retrieved.map {
      case (_, name, uni, country) => Lab(name, uni, country)
    }
  }

}

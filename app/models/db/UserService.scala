package models.db

import javax.inject.{Inject, Singleton}

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import models.User
import slick.jdbc.H2Profile.api._
import slick.lifted.TableQuery

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserService @Inject()(implicit ex: ExecutionContext) extends IdentityService[User] {

  val dbName = "h2file"
  val db = Database.forConfig(dbName)
  db.run(TableQuery[UserRecord].schema.create) // TODO


  def retrieve(loginInfo: LoginInfo): Future[Option[User]] = {
    val query = TableQuery[UserRecord].filter { _.providerEquals(loginInfo) }
    db.run(query.result).map { _.headOption }
  }
  def save(user: User): Future[Unit] = {
    val query = TableQuery[UserRecord]
    db.run( query += user ).map { _ => Unit }
  }
}
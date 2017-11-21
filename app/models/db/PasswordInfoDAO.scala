package models.db

import javax.inject.{Inject, Singleton}

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import slick.lifted.TableQuery

import scala.concurrent.{ExecutionContext, Future}
import LoginTableDefinitions._
import slick.jdbc.H2Profile.api._

@Singleton
class PasswordInfoDAO @Inject() (implicit ec: ExecutionContext) extends DelegableAuthInfoDAO[PasswordInfo]{

  val dbName = "h2file"
  val db = Database.forConfig(dbName)
  db.run(TableQuery[FullLoginInfoRecord].schema.create) // TODO

  private def upsert(loginInfo: LoginInfo, authInfo: PasswordInfo) = {
    val query = TableQuery[FullLoginInfoRecord]
    val action = query.insertOrUpdate( FullLoginInfo(
      loginInfo, authInfo))
    db.run(action).map { _ => authInfo }
  }

  def add(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = upsert(loginInfo, authInfo)
  def save(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = upsert(loginInfo, authInfo)
  def update(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = upsert(loginInfo, authInfo)

  def remove(loginInfo: LoginInfo): Future[Unit] = {
    val query = TableQuery[FullLoginInfoRecord]
    val action = query.filter { _.providerEquals(loginInfo) }.delete
    db.run(action).map { _ => Unit }
  }

  def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = {
    val query = TableQuery[FullLoginInfoRecord]
    val action = query.filter { _.providerEquals(loginInfo) }.result
    db.run(action). map {
      _.headOption.map { _.passwordInfo }
    }
  }
}

package models.db

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import models.User
import slick.jdbc.H2Profile.api._

class UserRecord (tag: Tag) extends Table[User](tag, "USERS") {
  def id = column[UUID]("ID", O.PrimaryKey)
  def providerId = column[String]("PROVIDER_ID")
  def providerKey = column[String]("PROVIDER_KEY")
  def name = column[String]("NAME")
  def country = column[String]("COUNTRY")
  def email = column[String]("EMAIL")

  def loginInfo = (providerId, providerKey) <> (LoginInfo.tupled, LoginInfo.unapply)
  def * = (id, loginInfo, name, country, email) <> (User.tupled, User.unapply)

  def providerEquals(loginInfo: LoginInfo) =
    providerId === loginInfo.providerID && providerKey === loginInfo.providerKey
}

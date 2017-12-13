package models.db

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import models.UserType.UserType
import models.{User, UserType}
import slick.jdbc.H2Profile.api._

class UserRecord (tag: Tag) extends Table[User](tag, "USERS") {

  implicit val enumMapper = MappedColumnType.base[UserType, String](_.toString, UserType.withName)

  def id = column[UUID]("ID", O.PrimaryKey)
  def providerId = column[String]("PROVIDER_ID")
  def providerKey = column[String]("PROVIDER_KEY")
  def name = column[String]("NAME")
  def country = column[String]("COUNTRY")
  def email = column[String]("EMAIL")
  def userType = column[UserType]("TYPE")

  def loginInfo = (providerId, providerKey) <> (LoginInfo.tupled, LoginInfo.unapply)

  def * = (id, loginInfo, name, country, email, userType) <> (User.tupled, User.unapply)
  def pk = index("PROVIDER_ID_KEY", (loginInfo), unique = true)

  def providerEquals(loginInfo: LoginInfo) =
    providerId === loginInfo.providerID && providerKey === loginInfo.providerKey
}

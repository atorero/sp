package models.db

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import slick.jdbc.H2Profile.api._

object LoginTableDefinitions {

  case class FullLoginInfo (
                       loginInfo: LoginInfo,
                       passwordInfo: PasswordInfo
                    )

  class FullLoginInfoRecord(tag: Tag) extends Table[FullLoginInfo](tag, "LOGIN_INFO") {
    def providerId = column[String]("PROVIDER_ID")
    def providerKey = column[String]("PROVIDER_KEY")
    def hasher = column[String]("HASHER")
    def password = column[String]("PASSWORD")
    def salt = column[Option[String]]("SALT")

    def loginInfo = (providerId, providerKey) <> (LoginInfo.tupled, LoginInfo.unapply)
    def passwordInfo = (hasher, password, salt) <> (PasswordInfo.tupled, PasswordInfo.unapply)

    def * = (loginInfo, passwordInfo) <> (FullLoginInfo.tupled, FullLoginInfo.unapply)
    def pk = primaryKey("PROVIDER_ID_KEY", (loginInfo))

    def providerEquals(loginInfo: LoginInfo) =
      providerId === loginInfo.providerID && providerKey === loginInfo.providerKey
  }

}

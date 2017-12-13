package models

import java.util.UUID

import com.mohiva.play.silhouette.api.{Identity, LoginInfo}
import UserType.UserType

case class User(
               id: UUID,
               loginInfo: LoginInfo,  // name: Option[String]
               name: String,
               country: String,
               email: String,
               userType: UserType
               ) extends Identity

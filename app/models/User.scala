package models

import java.util.UUID
import com.mohiva.play.silhouette.api.{ Identity, LoginInfo }

case class User(
               id: UUID,
               loginInfo: LoginInfo,  // name: Option[String]
               name: String,
               country: String,
               email: String
               ) extends Identity

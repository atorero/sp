package models

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService

import scala.concurrent.Future

class UserService extends IdentityService[User] {

  def retrieve(loginInfo: LoginInfo): Future[Option[User]] = ???
  def save(user: User) = ???
}

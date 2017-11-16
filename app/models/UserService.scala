package models

import javax.inject.{Inject, Singleton}

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}
import collection.JavaConverters._
import java.util.UUID

@Singleton
class UserService @Inject()(implicit ex: ExecutionContext) extends IdentityService[User] {

  def retrieve(loginInfo: LoginInfo): Future[Option[User]] = Future{ UserService.users.get(loginInfo) }
  def save(user: User) = UserService.users += (user.loginInfo -> user)
}

object UserService {

  /**
    * The list of users.
    */
  val users = new java.util.concurrent.ConcurrentHashMap[LoginInfo, User]().asScala
}
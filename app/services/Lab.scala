package services

/**
  * Created by amikhaylov8 on 10.10.17.
  */
case class Lab(
         id: Int,
         name: String,
         university: String,
         country: String
         ) {
}

object Lab {
  def apply(name: String, university: String, country: String): Lab = Lab(-1, name, university, country)
}

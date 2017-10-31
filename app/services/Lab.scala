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

  type LabTuple = (Int, String, String, String)

  def apply(tuple: LabTuple): Lab = {
    val (id, name, university, country) = tuple
    Lab(id, name, university, country)
  }

  def apply(name: String, university: String, country: String): Lab = Lab(-1, name, university, country)
}

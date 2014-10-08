package models

import anorm.SqlParser._
import anorm._
import org.kapunga.fredconnect._

case class FencerInfo(uid: Long) {
  def this(fredFencer: Fencer) = {
    this(-1)
  }
}

object FencerInfo {
  val DB_NAME = "club"

  val fredFencer = {
    get[Int]("fid") ~
    get[String]("usfa_id") ~
    get[String]("cff_id") ~
    get[String]("fie_id") ~
    get[String]("first_name") ~
    get[String]("last_name") ~
    get[String]("gender") ~
    get[Int]("birth_year") ~
    get[Int]("div_id") ~
    get[String]("club_ids") ~
    get[String]("f_rating") ~
    get[String]("e_rating") ~
    get[String]("s_rating") map {
      case fid ~ usfaId ~ cffId ~ fieId ~ firstName ~ lastName ~ gender ~
        birthYear ~ divId ~ clubIds ~ f_rating ~ e_rating ~ s_rating => {
        val authId = new AuthorityId(usfaId, cffId, fieId)
        val person = new Person(firstName, lastName, Gender.Male, birthYear)

      }
    }
  }

}



package models

import anorm._
import anorm.SqlParser._
import org.kapunga.fredconnect.{Club, Division}
import play.api.db.DB
import play.api.Play.current

object AuxFredInfo {
  val DB_NAME = "club"

  val club = {
    get[Int]("fid") ~
    get[String]("club_name") ~
    get[String]("club_abbrev") map {
      case fid ~ clubName ~ clubAbbrev =>
        Club(fid, clubName, clubAbbrev)
    }
  }

  val division = {
    get[Int]("fid") ~
    get[String]("div_name") ~
    get[String]("div_abbrev") map {
      case fid ~ divName ~ divAbbrev =>
        Division(fid, divName, divAbbrev)
    }
  }

  def saveClub(club: Club): Option[Club] = {
    DB.withConnection(DB_NAME) { implicit c =>
      SQL("INSERT INTO club (fid, club_name, club_abbrev) "
          + "VALUES ({fid}, {clubName}, {clubAbbrev}").on(
        'fid -> club.id,
        'clubName -> club.name,
        'clubAbbrev -> club.initials
      ).executeUpdate()
    }

    getClub(club.id)
  }

  def getClub(clubId: Int): Option[Club] = {
    DB.withConnection(DB_NAME) { implicit c =>
      SQL("SELECT * FROM club WHERE fid = {fid}").on('fid -> clubId).singleOpt(club)
    }
  }

  def getClubByAbbrev(abbrev: String): Option[Club] = {
    DB.withConnection(DB_NAME) { implicit c =>
      SQL("SELECT * FROM club WHERE club_abbrev = {clubAbbrev}").on('clubAbbrev -> abbrev).singleOpt(club)
    }
  }

  def saveDivision(division: Division): Option[Division] = {
    DB.withConnection(DB_NAME) { implicit c =>
      SQL("INSERT INTO division (fid, div_name, div_abbrev) "
        + "VALUES ({fid}, {divName}, {divAbbrev}").on(
          'fid -> division.id,
          'divName -> division.name,
          'divAbbrev -> division.abbrev
        ).executeUpdate()
    }

    getDivision(division.id)
  }

  def getDivision(divId: Int): Option[Division] = {
    DB.withConnection(DB_NAME) { implicit c =>
      SQL("SELECT * FROM division WHERE fid = {fid}").on('fid -> divId).singleOpt(division)
    }
  }

  def getDivisionByAbbrev(abbrev: String): Option[Division] = {
    DB.withConnection(DB_NAME) { implicit c =>
      SQL("SELECT * FROM division WHERE abbrev = {divAbbrev}").on('divAbbrev -> abbrev).singleOpt(division)
    }
  }
}

package models


import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current

case class Announcement(identifier: String, title: String, content: String, aid: Int = -1)

case class AnnouncementGroup(gid: Int, name: String)

object Announcement {
  val DB_NAME = "club"

  val announcement = {
    get[Int]("aid") ~
    get[String]("identifier") ~
    get[String]("title") ~
    get[String]("content") map {
      case aid ~ identifier ~ title ~ content =>
        Announcement(identifier, title, content, aid)
    }
  }

  val announcementGroup = {
    get[Int]("gid") ~
    get[String]("name") map {
      case gid ~ name =>
        AnnouncementGroup(gid, name)
    }
  }

  // DAO Methods for Announcements

  def create(announcement: Announcement): Option[Announcement] = {
    findByIdentifier(announcement.identifier) match {
      case Some(announcement) => None
      case None => {
        DB.withConnection(DB_NAME) { implicit c =>
          SQL("INSERT INTO announcement (identifier, title, content) "
            + "VALUES ({identifier}, {title}, {content})").on(
              'identifier -> announcement.identifier,
              'title -> announcement.title,
              'content -> announcement.content
            ).executeUpdate()
        }

        findByIdentifier(announcement.identifier)
      }
    }
  }

  def update(announcement: Announcement): Option[Announcement] = {
    findById(announcement.aid) match {
      case None => None
      case Some(announcement) => {
        DB.withConnection(DB_NAME) { implicit c =>
          SQL("UPDATE announcement SET (identifier, title, content) "
            + "VALUES ({identifier}, {title}, {content}) "
            + "WHERE aid = {aid}").on(
              'aid -> announcement.aid,
              'identifier -> announcement.identifier,
              'title -> announcement.title,
              'content -> announcement.content
            ).executeUpdate()

          findById(announcement.aid)
        }
      }
    }
  }

  def delete(aid: Long) = {
    DB.withConnection(DB_NAME) { implicit c =>
      SQL("DELETE FROM announce_order WHERE aid = {aid}").on('aid -> aid).executeUpdate()
      SQL("DELETE FROM announcement WHERE aid = {aid}").on('aid -> aid).executeUpdate()
    }
  }

  def findByIdentifier(identifier: String): Option[Announcement] = {
    DB.withConnection(DB_NAME) { implicit c =>
      SQL("SELECT * FROM announcement WHERE identifier = {identifier}").on('identifier -> identifier).singleOpt(announcement)
    }
  }

  def findById(aid: Long): Option[Announcement] = {
    DB.withConnection(DB_NAME) { implicit c =>
      SQL("SELECT * FROM announcement WHERE aid = {aid}").on('aid -> aid).singleOpt(announcement)
    }
  }

  def findAll(): List[Announcement] = {
    DB.withConnection(DB_NAME) { implicit c =>
      SQL("SELECT * FROM announcement").as(announcement *)
    }
  }

  // DAO Methods for Announcement Groups

  def createGroup(name: String): Option[AnnouncementGroup] = {
    findGroupByName(name) match {
      case Some(announcement) => None
      case None => {
        DB.withConnection(DB_NAME) { implicit c =>
          SQL("INSERT INTO announce_group (name) VALUES ({name})").on('name -> name).executeUpdate()
        }

        findGroupByName(name)
      }
    }
  }

  def updateGroup(group: AnnouncementGroup): Option[AnnouncementGroup] = {
    findGroupById(group.gid) match {
      case None => None
      case Some(group) => {
        DB.withConnection(DB_NAME) { implicit c =>
          SQL("UPDATE announce_group SET name = {name} "
            + "WHERE gid = {gid}").on(
              'gid -> group.gid,
              'name -> group.name
            ).executeUpdate()

          findGroupById(group.gid)
        }
      }
    }
  }

  def deleteGroup(gid: Int) = {
    DB.withConnection(DB_NAME) { implicit c =>
      SQL("DELETE FROM announce_order WHERE gid = {gid}").on('gid -> gid).executeUpdate()
      SQL("DELETE FROM announce_gid WHERE gid = {gid}").on('gid -> gid).executeUpdate()
    }
  }

  def findGroupById(gid: Int): Option[AnnouncementGroup] = {
    DB.withConnection(DB_NAME) { implicit c =>
      SQL("SELECT * FROM announce_group WHERE gid = {gid}").on('gid -> gid).singleOpt(announcementGroup)
    }
  }

  def findGroupByName(name: String): Option[AnnouncementGroup] = {
    DB.withConnection(DB_NAME) { implicit c =>
      SQL("SELECT * FROM announce_group WHERE name = {name}").on('name -> name).singleOpt(announcementGroup)
    }
  }

  def findAllGroups(): List[AnnouncementGroup] = {
    DB.withConnection(DB_NAME) { implicit c =>
      SQL("SELECT * FROM announce_group").as(announcementGroup *)
    }
  }

  // DAO Methods for grabbing and saving ordered groups of Announcements

  def updateGroupOrdering(gid: Int, groupOrder: List[Announcement]) {
    DB.withConnection(DB_NAME) { implicit c =>
      SQL("DELETE FROM announce_order WHERE gid = {gid}").on('gid -> gid).executeUpdate()

      var ordinal: Int = 0;
      groupOrder.foreach({ a =>
        SQL("INSERT INTO announce_order (ordinal, gid, aid) "
          + "VALUES ({ordinal}, {gid}, {aid})").on(
            'ordinal -> ordinal,
            'gid -> gid,
            'aid -> a.aid
          ).executeUpdate()
        ordinal = ordinal + 1
      })
    }
  }

  def getOrderedGroup(gid: Int): List[Announcement] = {
    DB.withConnection(DB_NAME) { implicit c =>
      SQL("SELECT a.* FROM announcement a INNER JOIN "
        + "announce_order o ON a.aid = o.aid "
        + "AND o.gid = {gid} ORDER BY ordinal").on(
          'gid -> gid
        ).as(announcement *)
    }
  }
}

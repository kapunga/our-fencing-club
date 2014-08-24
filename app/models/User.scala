package models

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current

/**
 * This case class contains basic user information for logging in, administrating,
 * being a coach, etc.
 *
 * @param username
 * @param email
 * @param password
 * @param uid
 * @param active
 * @param admin
 * @param isCoach
 */
case class User(username: String, email: String, password: String, uid: Long = -1,
                active: Boolean = false, admin: Boolean = false, isCoach: Boolean = false) {
  /**
   * Checks if the passwords match.
   *
   * @param testPassword
   * @return
   */
  def checkPassword(testPassword: String): Boolean = testPassword == password

  def screenInfo: UserScreenInfo = new UserScreenInfo(username, admin, isCoach)
}

/**
 * The Companion Object/DAO for the Users
 */
object User {
  val DB_NAME = "club"
  /**
   * User parser for the database
   */
  val user = {
      get[Long]("uid") ~
      get[String]("username") ~
      get[String]("email") ~
      get[String]("password") ~
      get[Boolean]("active") ~
      get[Boolean]("admin") ~
      get[Boolean]("is_coach") map {
        case uid ~ username ~ email ~ password ~ active ~ admin ~ isCoach =>
          User(username, email, password, uid, active, admin, isCoach)
      }
  }

  /**
   * Creates a new user from basic info.  This should be used to sign up users as it
   * defaults to inactive, not and admin, not a coach, etc.
   *
   * @param username
   * @param email
   * @param password
   * @return
   */
  def create(username: String, email: String, password: String): Option[User] = {
    create(new User(username, email, password))
  }

  /**
   * Creates a new user from a full User object.  This should only be used to bootstrap
   * users into the system.
   *
   * @param user
   * @return
   */
  def create(user: User): Option[User] = {
    findByName(user.username) match {
      case Some(user) => None
      case None => {
        DB.withConnection(DB_NAME) { implicit c =>
          SQL("INSERT INTO user (username, email, password, active, admin, is_coach) "
              + "VALUES ({username}, {email}, {password}, {active}, {admin}, {isCoach})").on(
            'username -> user.username,
            'email -> user.email,
            'password -> user.password,
            'active -> user.active,
            'admin -> user.admin,
            'isCoach -> user.isCoach
          ).executeUpdate()
        }

        findByName(user.username)
      }
    }
  }

  /**
   * Updates a user's info
   *
   * @param user
   * @return
   */
  def update(user: User): Option[User] = {
    findById(user.uid) match {
      case None => None
      case Some(user) => {
        DB.withConnection(DB_NAME) { implicit c =>
          SQL("UPDATE user SET (email, password, active, admin, is_coach) "
              + "VALUES ({email}, {password}, {active}, {admin}, {isCoach}) "
              + "WHERE uid = {uid}").on(
              'uid -> user.uid,
              'email -> user.email,
              'password -> user.password,
              'active -> user.active,
              'admin -> user.admin,
              'isCoach -> user.isCoach
            ).executeUpdate()

          findById(user.uid)
        }
      }
    }
  }

  /**
   * Deletes a user's basic info
   *
   * @param uid
   * @return
   */
  def delete(uid: Long) = {
    DB.withConnection(DB_NAME) { implicit c =>
      SQL("DELETE FROM user WHERE uid = {uid}").on('uid -> uid).executeUpdate()
    }
  }

  /**
   * Finds a user by username.
   *
   * @param username
   * @return
   */
  def findByName(username: String): Option[User] = {
    DB.withConnection(DB_NAME) { implicit c =>
      SQL("SELECT * FROM user WHERE username = {username}").on('username -> username).singleOpt(user)
    }
  }

  /**
   * Finds a user by user id
   *
   * @param uid
   * @return
   */
  def findById(uid: Long): Option[User] = {
    DB.withConnection(DB_NAME) { implicit c =>
      SQL("SELECT * FROM user WHERE uid = {uid}").on('uid -> uid).singleOpt(user)
    }
  }

  /**
   * Finds all coaches
   *
   * @return
   */
  def findAllAdmins: List[User] = {
    DB.withConnection(DB_NAME) { implicit c =>
      SQL("SELECT * FROM user WHERE admin = true").as(user *)
    }
  }

  /**
   * Finds all coaches
   *
   * @return
   */
  def findAllCoaches: List[User] = {
    DB.withConnection(DB_NAME) { implicit c =>
      SQL("SELECT * FROM user WHERE is_coach = true").as(user *)
    }
  }

  /**
   * Finds all inactive users.
   *
   * @return
   */
  def findAllInactiveUsers: List[User] = {
    DB.withConnection(DB_NAME) { implicit c =>
      SQL("SELECT * FROM user WHERE active = false").as(user *)
    }
  }
}

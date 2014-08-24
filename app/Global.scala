import models.User
import play.api._

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Application has started")

    // Bootstrap in admins if necessary
    if (User.findAllAdmins.size == 0) {
      Logger.info("Bootstrapping in an admin...")

      val kapunga = new User("kapunga", "kapunga@gmail.com", "omgwtfbbq", active=true, admin=true)

      User.create(kapunga) match {
        case Some(newAdmin) => Logger.info("Created new admin: " + newAdmin.username + " uid: " + newAdmin.uid)
        case None => Logger.error("Couldn't create an admin, site has no admins.")
      }
    } else {
      User.findAllAdmins.foreach(user => Logger.info("Found admin: " + user.username + " uid: " + user.uid))
    }
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }
}

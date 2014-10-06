import models.{Announcement, User}
import play.api._
import org.kapunga.fredconnect.AskFredClient

object Global extends GlobalSettings {
  val bootAdmins = List("kapunga@gmail.com")
  val bootCoaches = List("daniel.hondor@gmail.com")

  val fredKey = "4ce7dab47e291bd2e85fb154f9b5f20b"
  val clubId = 6859

  val askFredClient = new AskFredClient(fredKey)

  override def onStart(app: Application) {
    Logger.info("Application has started")

    // Bootstrap in admins if necessary
    if (User.findAllAdmins.size == 0) {
      Logger.info("Bootstrapping in an admin...")

      val kapunga = new User("kapunga", "kapunga@gmail.com", "omgwtfbbq", active=true, admin=true)

      User.create(kapunga) match {
        case None => Logger.error("Couldn't create an admin, site has no admins.")
        case Some(newAdmin) => Logger.info("Created new admin: " + newAdmin.username + " uid: "
                                          + newAdmin.uid + " password: " + newAdmin.password)
      }
    } else {
      User.findAllAdmins.foreach(user => Logger.info("Found admin: " + user.username + " uid: " + user.uid))
    }

    // Bootstrap in some screens
    if (Announcement.findAll().size == 0) {
      Logger.info("Bootstrapping in some default screens...")

      val announcementOne = new Announcement("Welcome",
                                             "Welcome to the Olympia Fencing Center Open House!",
                                             "<p>Feel free to look around and observe.  All our fencers "
                                             + "are very friendly and would be happy to explain what is "
                                             + "going on, so don't be shy about asking!</p>")
      val announcementTwo = new Announcement("Hello",
                                             "Say Hello!",
                                             "<p>Don't hesitate to introduce yourself to any  "
                                             + "are very friendly and would be happy to explain what is "
                                             + "going on, so don't be shy about asking!</p>")
      
      Announcement.create(announcementOne) match {
        case None => Logger.error("Couldn't create first bootstrapped announcement.")
        case Some(announcement) => Logger.info("Created new announcement: " + announcement.identifier 
                                               + " aid:" + announcement.aid)
      }

      Announcement.create(announcementTwo) match {
        case None => Logger.error("Couldn't create second bootstrapped announcement.")
        case Some(announcement) => Logger.info("Created new announcement: " + announcement.identifier
          + " aid:" + announcement.aid)
      }
    }

    // Bootstrap in some screen groups
    if (Announcement.findAllGroups().size == 0) {
      Logger.info("Bootstrapping in a default screen group...")

      Announcement.createGroup("Default Screens") match {
        case None => Logger.error("Couldn't create a screen group, site can't display screens.")
        case Some(group) => {
          Logger.info("Created new group: " + group.name)
          
          Announcement.updateGroupOrdering(group.gid, Announcement.findAll())

          Logger.info("Bootstrapped in a Group ordering for default group.")
        } 
      }
    }

    Logger.info("Fetching club members...")

    val clubFencers = askFredClient.getFencersFromClub(clubId)

    clubFencers.foreach(fencer => println(s"Found fencer ${fencer.person.firstName} ${fencer.person.lastName}"))
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }
}

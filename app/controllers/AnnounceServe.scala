package controllers

import play.api.Logger
import play.api.mvc.{Action, Controller}
import play.api.templates.Html
import models.Announcement

object AnnounceServe extends Controller {
  def firstScreen = nextScreen(0)

  def nextScreen(ordinal: Int) = Action {
    val screenList: List[Announcement] = Announcement.getOrderedGroup(1);

    val nextScreen: Int = {
      if (ordinal + 1 >= screenList.size) 0
      else ordinal + 1
    }

    val screen = {
      if (screenList.size > ordinal && ordinal >= 0) {
        screenList(ordinal)
      } else {
        screenList(0)
      }
    }

    Ok(views.html.announce(screen.title, nextScreen)(Html(screen.content)))
  }
}

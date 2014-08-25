package controllers

import play.api.mvc.{Action, Controller}

object AnnounceServe extends Controller {
  def nextScreen = Action {
    Ok(views.html.announce())
  }

}

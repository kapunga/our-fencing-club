package controllers

import play.api.mvc._

object Application extends Controller with Secured {

  def index = withUser { user => implicit request =>
    Ok(views.html.index(user.screenInfo))
  }
}
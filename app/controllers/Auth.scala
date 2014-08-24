package controllers

import play.api.mvc.{Security, Action, Controller}
import play.api.data.Form
import play.api.data.Forms._
import models.User
import play.api.Logger

object Auth extends Controller {
  val loginForm = Form(
    mapping(
      "Username" -> text,
      "Password" -> text
    )(UserLoginData.apply)(UserLoginData.unapply) verifying("Username or password incorrect.", _.validate())
  )

  def loginPage = Action { implicit request =>
    request.session.get(Security.username) match {
      case Some(username) => Redirect("/")
      case None => Ok(views.html.login(loginForm))
    }
  }

  def doLogin = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.login(formWithErrors)),
      userLoginData => {
        User.findByName(userLoginData.username) match  {
          case Some(user) => Ok(views.html.index(user.screenInfo)).withSession((Security.username, user.username))
          case None => {
            Logger.error("Found user: " + userLoginData.username + " but couldn't log them in.")
            InternalServerError("There is an error logging you in, please try again.")
          }
        }
      }
    )
  }

  def logout = Action {
    Ok(views.html.logout()).withNewSession
  }
}

case class UserLoginData(username: String, password: String) {
  def validate(): Boolean = {
    User.findByName(username) match {
      case Some(user) => {
        user.checkPassword(password)
      }
      case None => {
        false
      }
    }
  }
}

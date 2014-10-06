package controllers

import play.api.mvc.{Action, Controller}
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json._
import models.User

object UserManager extends Controller {
  val signupForm = Form(
    mapping(
      "Username" -> nonEmptyText,
      "Email" -> email,
      "Verify Email" -> email,
      "Password" -> nonEmptyText(minLength = 6),
      "Verify Password" -> nonEmptyText(minLength = 6)
    )(UserSignupData.apply)(UserSignupData.unapply).verifying("Emails do not match!",
        _.validateEmail()).verifying("Passwords do not match!", _.validatePassword())
  )

  def signupPage = Action {
    Ok(views.html.signup(signupForm))
  }

  def doSignup = Action { implicit request =>
    signupForm.bindFromRequest().fold(
      formWithErrors => BadRequest(views.html.signup(formWithErrors)),
      userSignupData => {
        User.create(
          userSignupData.username,
          userSignupData.email,
          userSignupData.password
        )
        Redirect("/login")
      }
    )
  }

  def userManagement = TODO

  def getInactiveUserCount = Action {
    val inactives = User.findAllInactiveUsers

    Ok(toJson(
      Map("count" -> inactives.length)
    ))

  }
}

case class UserSignupData(username: String, email: String, verifyEmail: String,
                          password: String, verifyPassword: String) {
  def validateEmail(): Boolean = email == verifyEmail

  def validatePassword(): Boolean = password == verifyPassword
}

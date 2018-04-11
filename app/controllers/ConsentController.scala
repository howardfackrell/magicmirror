package controllers

import javax.inject.Inject

import play.api.libs.json.Json
import play.api.mvc._

import scala.collection.mutable

class ConsentController @Inject()(cc: ControllerComponents)
  extends AbstractController(cc) {

  var consent = mutable.Map[String, Boolean]()

  def needed(userId: String) = Action {
    implicit request: Request[AnyContent] =>
      val hasConsented = consent.getOrElse(userId, false)
      Ok(Json.obj("hasAgreed" -> hasConsented))
  }

  def reset(userId: String) = Action {
    implicit request: Request[AnyContent] =>
      consent.remove(userId)
      Ok(s"consent cleared for $userId")
  }

  def resetAll() = Action {
    implicit request: Request[AnyContent] =>
      consent.clear()
      Ok("consents have been cleared")
  }

  def getConsent(userId: String) = Action {
    implicit request: Request[AnyContent] =>
      val returnTo :Option[String] = request.queryString.get("returnTo").flatMap(_.headOption)
      System.out.println(s"returnTo $returnTo")
      Ok(views.html.consent(userId, returnTo.getOrElse("https://www.google.com")))
  }

  def received(userId: String) = Action {
    implicit request: Request[AnyContent] =>
      consent.put(userId, true)
      val redirectUrl: Option[String] = request.body.asFormUrlEncoded
          .flatMap {
            form =>
              form.get("returnTo").flatMap(seq => seq.headOption)
          }
      println(s"Received agreement from $userId, redirect to $redirectUrl")
      redirectUrl.map {
        url => Redirect(url, 302)
      }.getOrElse {
        Ok("Consent received")
      }
  }
}

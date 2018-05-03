package controllers

import java.util.UUID
import javax.inject.Inject

import data.{Subject, PolicySession, PolicySessionRequest, PolicySessionState}
import data.implicits._
import play.api.libs.json._
import play.api.mvc._

import scala.collection.mutable

class PolicyController @Inject()(cc: ControllerComponents)
  extends AbstractController(cc) {


  var consent = mutable.Map[String, Boolean]()

  var sessionStore = mutable.Map[String, PolicySessionState]()

  def createSession = Action {
    implicit request: Request[AnyContent] =>

      val urlbase = request.uri

      val sessionRequestOpt: Option[PolicySessionRequest] = request.body.asJson.flatMap[PolicySessionRequest] {
        body => Json.fromJson[PolicySessionRequest](body).asOpt
      }

      sessionRequestOpt.map {
        sessionRequest: PolicySessionRequest =>
          val sessionId = UUID.randomUUID().toString
          val employeeId = sessionRequest.subject.employeeId
          val userConsent = consent.getOrElseUpdate(employeeId, false)
          sessionStore.put(sessionId, PolicySessionState(false, "date", sessionRequest, sessionRequest.state))

          val session: PolicySession = PolicySession(!userConsent, urlbase, sessionId)
//          Created(session)
          Created("JUST WORK")
      }.getOrElse(BadRequest("Couldn't parse the Json: "))
  }

  def getSession(sessionToken: String) = Action {
    implicit request: Request[AnyContent] =>
      sessionStore.get(sessionToken).map {
        sessionState =>
//          Ok(sessionState)
          Ok("Just compile")
      }.getOrElse(NotFound(s"no session with toke $sessionToken"))
  }

  def getSessionConsent(sessionToken: String) = Action {
    implicit request: Request[AnyContent] =>
      Ok(views.html.sessionconsent(sessionToken))
  }

  def consentReceived(sessionToken: String) = Action {
    implicit request: Request[AnyContent] =>
      sessionStore.get(sessionToken).map {
        session =>
          consent.remove(session.sessionRequest.subject.employeeId)
          session.sessionRequest.returnTo
      }.map {
        url => Redirect(url, 302)
      }.getOrElse {
        Ok("Consent received")
      }
  }
}
package data

import play.api.libs.json.Json


case class Subject(
                    val customerId: String,
                    val employeeId: String,
                    val clientId: String) {}

case class PolicySessionRequest(
                                 val subject: Subject,
                                 val state: Map[String, String],
                                 val returnTo: String) {}

case class PolicySession(
                          val redirect: Boolean,
                          val destination: String,
                          val sessionId: String) {}

case class PolicySessionState(
                               val accepted: Boolean,
                               val acceptedOn: String,
                               val sessionRequest: PolicySessionRequest,
                               val state: Map[String, String]) {}

object implicits {
//  implicit val subjectFormat: OFormat[Subject] = Json.format[Subject]
//  implicit val policySessionRequestFormat: OFormat[PolicySessionRequest] = Json.format[PolicySessionRequest]
//  implicit val policySessionFormat: OFormat[PolicySession] = Json.format[PolicySession]
//  implicit val policySessionStateFormat: OFormat[PolicySessionState] = Json.format[PolicySessionState]

  implicit val subjectFormat = Json.format[Subject]
  implicit val policySessionRequestFormat = Json.format[PolicySessionRequest]
  implicit val policySessionFormat = Json.format[PolicySession]
  implicit val policySessionStateFormat = Json.format[PolicySessionState]
}

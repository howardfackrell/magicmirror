package controllers

import javax.inject.Inject

import play.api.libs.json.Json
import play.api.mvc._

import scala.collection.mutable

class YearbookApiController @Inject()(cc: ControllerComponents)
  extends AbstractController(cc) {

  val logins = mutable.Map[String, (String, String)]()


  def getLogins() = Action {
    implicit request: Request[AnyContent] =>
      System.out.println("logins: " + logins)
      Ok(views.html.yearbookapi(logins.toMap))
  }

//  def createLogin(configuredPath: String) = Action {
//    implicit request: Request[AnyContent] =>
//      val pair : Option[(String, String)] = request.body.asJson.map {
//        js =>
//          ((js \ "lastName").as[String], (js \ "accessNumber").as[String])
//      }
//
//      pair.fold(BadRequest("failed to create the login")) {
//        userAccessCodePair =>
//          val (userName, accessCode) = userAccessCodePair
//          logins.put(configuredPath, (userName, accessCode))
//          Created("created")
//      }
//  }

  def createLogin() = Action  {
    implicit request: Request[AnyContent] =>
      println("create a login!!!")
      val configuredPath = request.body.asFormUrlEncoded.get.get("configuredPath").get.head
      val lastName = request.body.asFormUrlEncoded.get.get("userName").get.head
      val accessNumber = request.body.asFormUrlEncoded.get.get("accessNumber").get.head
      System.out.println("before " + logins)
      logins.put(configuredPath, (lastName, accessNumber))
      System.out.println("after " + logins)
      Ok(views.html.yearbookapi(logins.toMap))
  }

  def deleteLogin(configuredPath: String) = Action {
    implicit request: Request[AnyContent] =>
      logins.remove(configuredPath)
      Ok("deleted")
  }

  def getCredentials(configuredPath: String) = Action {
    implicit request: Request[AnyContent] =>
      val (lastName, accessNumber) = logins.get(configuredPath).get
      Ok(Json.obj("lastName" -> lastName, "accessNumber" -> accessNumber))
  }

}

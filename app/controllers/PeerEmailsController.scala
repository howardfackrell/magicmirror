package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc._
import play.api.libs.ws.WSClient

@Singleton
class PeerEmailsController @Inject()(ws: WSClient, cc: ControllerComponents) extends AbstractController(cc) {


}

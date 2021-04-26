package controllers

import javax.inject._

import play.api._
import play.api.libs.json._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def getRequest(path:String) = request(path)
  def postRequest(path:String) = request(path)
  def putRequest(path:String) = request(path)
  def patchRequest(path:String) = request(path)
  def deleteRequest(path:String) = request(path)
  def headRequest(path:String) = request(path)
  def optionsRequest(path:String) = request(path)

  def request(path: String) = Action {
    implicit request: Request[AnyContent] =>
      val headersAsJs = request.headers.headers.map(e => (e._1 -> JsString(e._2)))

      val headersJson = JsObject(headersAsJs)

      val cookiesJson = request.cookies.toList.map{
        cookie =>
          Json.obj(
            "domain" -> cookie.domain,
            "path" -> cookie.path,
            "path" -> cookie.name,
            "value" -> cookie.value,
            "secure" -> cookie.secure,
            "httpOnly" -> cookie.httpOnly
          )
      }

      val requestAsJson = Json.obj(
        "path" -> ("/request/" + path),
        "headers" -> headersJson,
        "body" -> body(request),
        "cookies" -> cookiesJson
      )

      Ok(requestAsJson)
  }

  def body(request: Request[AnyContent]) : JsValue = {
    val formBody = request.body.asFormUrlEncoded.map {
      form =>
      Json.obj("form" ->  {
          val jsonMap = form.map {
            entry =>
              val key: String = entry._1
              val values: Seq[String] = entry._2
              (key -> JsArray(values.map(JsString(_))))
          }
          JsObject(jsonMap)
      })
    }

    val textBody = request.body.asText.map {
      text =>
        Json.obj("text" -> (JsString(text)))
    }

    val jsonBody = request.body.asJson.map {
      json => Json.obj("json" -> json)
    }

    val xmlBody = request.body.asXml.map {
      xml =>
        Json.obj("xml" ->   xml.toString() )
    }

    val rawBody = request.body.asRaw.map {
      raw =>
      Json.obj("raw" -> raw.toString())
    }

    formBody
      .orElse(textBody)
      .orElse(jsonBody)
      .orElse(xmlBody)
      .orElse(rawBody)
      .getOrElse(Json.obj("other" -> "not form, text, json, xml, or raw"))

  }

  def external = Action {
    request =>

      Ok("that worked");
  }
}

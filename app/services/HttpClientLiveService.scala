package services

import javax.inject.{Inject, Singleton}

import models.{HttpRequest, HttpResponse}
import play.api.libs.ws.WSClient
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

@Singleton
class HttpClientLiveService @Inject()(ws: WSClient) extends HttpClientService {
  override def request(httpRequest: HttpRequest): Future[HttpResponse] = {
    ws.url(
      url = httpRequest.url
    ).withQueryString(
      httpRequest.queryString.getOrElse(Map.empty[String, String]).toArray[(String, String)]:_*
    ).withHeaders(
      httpRequest.headers.getOrElse(Map.empty[String, String]).toArray[(String, String)]:_*
    ).withMethod(
      method = httpRequest.method.toUpperCase
    ).withBody(
      body = httpRequest.body.getOrElse("")
    ).execute map { response =>
      HttpResponse(
        id = httpRequest.id,
        status = response.status,
        body = response.body
      )
    }
  }
}

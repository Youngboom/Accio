package services

import javax.inject.{Inject, Singleton}

import models.{HttpRequest, HttpResponse}
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.Future

@Singleton
class HttpClientMockService extends HttpClientService {
  override def request(httpRequest: HttpRequest): Future[HttpResponse] = {
    httpRequest.url match {
      case "http://test.com/success" =>
        Future successful HttpResponse(
          id = httpRequest.id,
          status = 200,
          body = "Success"
        )
      case "http://test.com/fail" =>
        Future successful HttpResponse(
          id = httpRequest.id,
          status = 400,
          body = "Fail"
        )
    }
  }
}

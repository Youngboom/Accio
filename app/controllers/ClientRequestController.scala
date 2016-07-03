package controllers

import javax.inject.{Inject, Singleton}

import actions.ClientRequestAction
import models.{ClientRequestLogItem, HttpResponse}
import play.api.libs.json.{Json, Writes}
import play.api.mvc.Controller
import services.{HttpClientService, LoggingService}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ClientRequestController @Inject()(httpClientService: HttpClientService, clientRequestAction: ClientRequestAction, loggingService: LoggingService)(implicit exec: ExecutionContext) extends Controller {
  implicit val ProductDTOWrites = new Writes[HttpResponse] {
    def writes(httpResponse: HttpResponse) = Json.obj(
      "id" -> httpResponse.id,
      "status" -> httpResponse.status,
      "body" -> httpResponse.body
    )
  }

  def request = clientRequestAction.async { implicit request =>
    Future sequence {
      request.httpRequests map { httpRequest =>
        for {
          httpResponse <- httpClientService.request(httpRequest)
          _ = loggingService.log(ClientRequestLogItem(
            ip = request.remoteAddress,
            url = httpRequest.url,
            status = httpResponse.status,
            method = httpRequest.method
          ))
        } yield HttpResponse(
          id = httpRequest.id,
          status = httpResponse.status,
          body = httpResponse.body
        )
      }
    } map { responses =>
      Ok(Json.toJson(responses))
    }
  }
}

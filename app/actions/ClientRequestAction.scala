package actions

import javax.inject.{Inject, Singleton}

import actions.requests.ClientRequest
import models.ClientRequestForm
import org.json4s.DefaultFormats
import play.api.mvc._
import play.api.mvc.Results._
import services.CipherService
import org.json4s.jackson.Serialization._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

@Singleton
class ClientRequestAction @Inject()(cipherService: CipherService) extends ActionBuilder[ClientRequest] with ActionRefiner[Request, ClientRequest] {
  implicit val formats = DefaultFormats

  override def refine[A](request: Request[A]): Future[Either[Result, ClientRequest[A]]] = {
    for {
      clientRequestsString <- cipherService.decrypt {
        request.body.asInstanceOf[AnyContentAsText].txt
      }
      clientRequest = Try(read[ClientRequestForm](clientRequestsString))
    } yield clientRequest match {
      case Success(r) =>
        if (System.currentTimeMillis > r.expireAt)
          Left {
            BadRequest(Json.toJson(Json.obj("Error" -> "Expired")))
          }
        else
          Right {
            ClientRequest(r.httpRequests, request)
          }
      case Failure(e) =>
        Left {
          BadRequest(Json.toJson(Json.obj("Error" -> "Invalid argument")))
        }
    }
  }
}

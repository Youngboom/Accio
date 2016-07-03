package actions.requests

import models.HttpRequest
import play.api.mvc.{Request, WrappedRequest}

case class ClientRequest[A](httpRequests: List[HttpRequest], request: Request[A]) extends WrappedRequest[A](request)

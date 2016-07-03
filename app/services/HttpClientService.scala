package services

import models.{HttpRequest, HttpResponse}

import scala.concurrent.Future

trait HttpClientService {
  def request(httpRequest: HttpRequest): Future[HttpResponse]
}

package models

case class ClientRequestForm(httpRequests: List[HttpRequest], expireAt: Long)

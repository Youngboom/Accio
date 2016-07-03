package models

case class HttpRequest(
  id: String,
  url: String,
  method: String,
  body: Option[String],
  headers: Option[Map[String, String]],
  queryString: Option[Map[String, String]]
)

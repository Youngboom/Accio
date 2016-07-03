package services

import javax.inject.{Inject, Singleton}

import models.ClientRequestLogItem
import org.json4s.DefaultFormats
import play.api.Logger
import org.json4s.jackson.Serialization._

@Singleton
class LoggingService @Inject()() {
  implicit val formats = DefaultFormats
  private val requestLogger = Logger("request")

  def log(clientRequestLogItem: ClientRequestLogItem): Unit = {
    requestLogger.trace(write[ClientRequestLogItem](clientRequestLogItem))
  }
}

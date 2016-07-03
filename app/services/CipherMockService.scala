package services

import javax.inject.{Inject, Singleton}

import play.api.Configuration

import scala.concurrent.Future

@Singleton
class CipherMockService @Inject()(configuration: Configuration) extends CipherService {

  override def encrypt(data: String): Future[String] = {
    Future successful data
  }

  override def decrypt(data: String): Future[String] = {
    Future successful data
  }
}

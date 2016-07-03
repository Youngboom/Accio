package services

import scala.concurrent.Future

trait CipherService  {
  def encrypt(data: String): Future[String]

  def decrypt(data: String): Future[String]
}

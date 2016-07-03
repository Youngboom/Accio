package services

import java.nio.charset.Charset
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.{IvParameterSpec, SecretKeySpec}
import javax.inject.{Inject, Singleton}

import play.api.Configuration

import scala.concurrent.Future

@Singleton
class CipherLiveService @Inject()(configuration: Configuration) extends CipherService {
  private val cipherForEncrypt: Cipher = cipher(Cipher.ENCRYPT_MODE)
  private val cipherForDecrypt: Cipher = cipher(Cipher.DECRYPT_MODE)
  private val base64Encoder = Base64.getUrlEncoder
  private val base64Decoder = Base64.getUrlDecoder

  private def cipher(mode: Int) = {
    val KeyString: String = configuration.getString("aesKey").get
    val key = new SecretKeySpec(KeyString.getBytes, 0, KeyString.getBytes.length, "AES")
    val iv: IvParameterSpec = new IvParameterSpec(KeyString.substring(0, 16).getBytes)
    val c = Cipher.getInstance("AES/CBC/PKCS5Padding")
    c.init(mode, key, iv)
    c
  }

  override def encrypt(data: String): Future[String] = {
    Future successful {
      base64Encoder.encodeToString {
        cipherForEncrypt.doFinal(data.getBytes(Charset.forName("utf-8")))
      }
    }
  }

  override def decrypt(data: String): Future[String] = {
    Future successful {
      new String(cipherForDecrypt.doFinal(base64Decoder.decode {
        data.getBytes(Charset.forName("utf-8"))
      }), Charset.forName("utf-8"))
    }
  }
}

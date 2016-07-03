import models.{ClientRequestForm, HttpRequest}
import org.json4s.DefaultFormats
import org.scalatestplus.play.{OneAppPerTest, PlaySpec}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.{CipherMockService, CipherService, HttpClientMockService, HttpClientService}
import org.json4s.jackson.Serialization._

import scala.concurrent.duration._

class IntegrateSpec extends PlaySpec with OneAppPerTest {
  implicit val formats = DefaultFormats

  val application = new GuiceApplicationBuilder()
    .overrides(bind[HttpClientService].to[HttpClientMockService])
    .overrides(bind[CipherService].to[CipherMockService])
    .build

  val properRequestParam = ClientRequestForm(
    httpRequests = List(
      HttpRequest(
        id = "1",
        url = "http://test.com/success",
        method = "get",
        body = None,
        headers = None,
        queryString = None
      ),
      HttpRequest(
        id = "2",
        url = "http://test.com/fail",
        method = "get",
        body = None,
        headers = None,
        queryString = None
      )
    ),
    expireAt = System.currentTimeMillis + 1.days.toMillis
  )

  "HomeController" should {
    "response properly" in {
      val param = write[ClientRequestForm](properRequestParam)

      val resp = route(application, FakeRequest(POST, "/").withBody(param)).get
      status(resp) mustBe OK
      contentAsString(resp) must include ("200")
      contentAsString(resp) must include ("400")
    }
    "response that request is expired" in {
      val param = write[ClientRequestForm](properRequestParam.copy(expireAt = System.currentTimeMillis))

      val resp = route(application, FakeRequest(POST, "/").withBody(param)).get
      status(resp) mustBe BAD_REQUEST
      contentAsString(resp) must include ("Error")
      contentAsString(resp) must include ("Expired")
    }

    "response that request parameter is invalid" in {
      val resp = route(application, FakeRequest(POST, "/").withBody("Invalid argument")).get
      status(resp) mustBe BAD_REQUEST
      contentAsString(resp) must include ("Error")
      contentAsString(resp) must include ("Invalid argument")
    }
  }
}

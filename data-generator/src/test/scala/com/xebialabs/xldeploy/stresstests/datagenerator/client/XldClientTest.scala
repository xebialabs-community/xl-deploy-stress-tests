package com.xebialabs.xldeploy.stresstests.datagenerator.client

import com.xebialabs.xldeploy.stresstests.datagenerator.domain.Directory
import com.xebialabs.xldeploy.stresstests.datagenerator.json.XldJsonProtocol
import com.xebialabs.xldeploy.stresstests.datagenerator.support.UnitTestSugar
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import spray.http.{HttpResponse, StatusCodes}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
@RunWith(classOf[JUnitRunner])
class XldClientTest extends UnitTestSugar with XldJsonProtocol {

  val client = new XldClient("http://localhost:4516/deployit")

  describe("XLD client") {
    it("should create a directory") {
      val dir1 = Directory("Environments/dir1")

      val createResponse = client.createCi(dir1).futureValue
      createResponse.status shouldBe StatusCodes.OK

      val removeResponse = client.removeCi(dir1.id).futureValue
      removeResponse.status shouldBe StatusCodes.NoContent
    }
  }

  def expectSuccessfulResponses(responsesFutures: Seq[Future[HttpResponse]]): Unit = {
    val releaseResponses = Future.sequence(responsesFutures).futureValue
    releaseResponses.foreach(releaseResponse =>
      releaseResponse.status.intValue should (be >= 200 and be < 300)
    )
  }

  def expectSuccessfulResponse(responseFuture: Future[HttpResponse]): Unit = {
    val response = responseFuture.futureValue
    response.status.intValue should (be >= 200 and be < 300)
  }
}

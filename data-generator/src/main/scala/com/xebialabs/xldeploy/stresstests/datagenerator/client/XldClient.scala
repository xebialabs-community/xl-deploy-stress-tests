package com.xebialabs.xldeploy.stresstests.datagenerator.client

import java.io.File
import java.util.concurrent.atomic.AtomicInteger

import akka.actor.ActorSystem
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging
import com.xebialabs.xldeploy.stresstests.datagenerator.domain.Ci
import com.xebialabs.xldeploy.stresstests.datagenerator.json.XldJsonProtocol
import spray.client.pipelining._
import spray.http._
import spray.httpx.SprayJsonSupport._
import spray.json._
import spray.http.MediaTypes._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps

object XldClient {

  /**
   * A wrapper for error messages extracted from non-successful responses.
   */
  class XldClientException(m: String) extends RuntimeException(m)

  /**
   * Returns a failed [[Future]] for all the non-successful responses.
   */
  private[client] def failNonSuccessfulResponses(responseFuture: Future[HttpResponse]) = responseFuture.flatMap {
    case response if response.status.isFailure =>
      Future.failed(new XldClientException(response.entity.data.asString))
    case _ =>
      responseFuture
  }
}

class XldClient(apiUrl: String, username: String = "admin", password: String = "admin") extends XldJsonProtocol with AdditionalFormats with LazyLogging {

  implicit val system: ActorSystem = ActorSystem()
  implicit val timeout: Timeout = Timeout(30 days)
  val requestCounter = new AtomicInteger(0)

  private val strictPipeline = (req: HttpRequest) => {
    val requestNum = requestCounter.getAndIncrement()
    val loggingReq = (i: HttpRequest) => {
      logger.debug(i.toString)
    }
    val loggingResp = (i: HttpResponse) => {
      logger.info(s"Request $requestNum execution done with ${i.status}")
      logger.debug(i.toString)
    }

    val pipeline = logRequest(loggingReq) ~>
      addCredentials(BasicHttpCredentials(username, password)) ~>
      sendReceive ~>
      logResponse(loggingResp)

    XldClient.failNonSuccessfulResponses(pipeline(req))
  }


  def createCi(ci: Ci): Future[HttpResponse] =
    strictPipeline(Post(s"$apiUrl/repository/ci/${ci.id}", ci))

  def removeCi(id: String): Future[HttpResponse] =
    strictPipeline(Delete(s"$apiUrl/repository/ci/$id"))

  def uploadPackage(packageName: String): Future[HttpResponse] = {
    val contentType = ContentType(`application/octet-stream`)
    val contentTypeHeader = HttpHeaders.`Content-Type`(contentType)
    val contentDispositionHeader = HttpHeaders.`Content-Disposition`("form-data", Map("name" -> "fileData", "filename" -> s"$packageName.dar"))
    val headerSeq = Seq(contentTypeHeader, contentDispositionHeader)
    val httpData = HttpData(new File("/Users/vinny/Desktop/PetClinic-ear-1.0.dar"))
    val mfd = new MultipartFormData(Seq(new BodyPart(HttpEntity(contentType, httpData), headerSeq)))
    strictPipeline(Post(s"$apiUrl/package/upload/$packageName.dar", mfd))
  }
}

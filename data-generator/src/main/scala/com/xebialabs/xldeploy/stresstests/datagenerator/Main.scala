package com.xebialabs.xldeploy.stresstests.datagenerator

import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigFactory.parseResources
import com.typesafe.scalalogging.LazyLogging
import com.xebialabs.xldeploy.stresstests.datagenerator.client.XldClient
import com.xebialabs.xldeploy.stresstests.datagenerator.domain._
import spray.http.HttpResponse

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Failure

object Main extends App with LazyLogging {

  val config = parseResources("data-generator.conf").withFallback(ConfigFactory.load())

  val client = new XldClient(
    config.getString("xld.data-generator.baseUrl"),
    config.getString("xld.data-generator.username"),
    config.getString("xld.data-generator.password"))

  var futures: Seq[Future[HttpResponse]] = Seq()

  val nrOfDirs = 10
  val nrOfDictsPerDir = 10
  futures ++= Range(0, nrOfDirs).map( dirNr => client.createCi(Directory(s"Environments/dir$dirNr")))
  futures ++= Range(0, nrOfDirs).flatMap( dirNr => createNDictionariesIn(nrOfDictsPerDir, s"Environments/dir$dirNr"))
  def createNDictionariesIn(n: Int, dirId: String) = {
    Range(0, n).map(dictNr => client.createCi(Dictionary(s"$dirId/dict$dictNr",
      Map("key1" -> "value1", "jdbc.url" -> "jdbc:oracle:thin:@localhost:1521:ORCL"),
      Map("encryptedKey1" -> "secret", "scott" -> "tiger"))))
  }

  val nrOfHosts = 10
  futures ++= Range(0, nrOfHosts).map( hostNr => client.createCi(SshHost(s"Infrastructure/host${hostNr}", "UNIX", "SCP", "overthere", "overthere", "overhere")))
  futures :+= client.createCi(Environment(s"Environments/${nrOfHosts}hosts", Range(0, nrOfHosts).map( hostNr => CiRef(s"Infrastructure/host${hostNr}", "overthere.SshHost")), Seq()))
  futures :+= client.createCi(Application("Applications/cmdapp1"))
  futures :+= client.createCi(DeploymentPackage("Applications/cmdapp1/v1"))
  futures :+= client.createCi(Command("Applications/cmdapp1/v1/cmd1", "date"))

  val allFutures = Future.sequence(futures)

  allFutures.andThen {
    case Failure(ex) =>
      logger.error("Could not generate data set: ", ex)
  } andThen {
    case _ =>
      logger.debug("Shutting down the actor system after everything has been done.")
      client.system.shutdown()
      client.system.awaitTermination()
  }
}

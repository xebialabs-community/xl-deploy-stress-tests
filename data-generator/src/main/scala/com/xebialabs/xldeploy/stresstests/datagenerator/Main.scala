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

  // create directories and dictionaries for readRepositoryScenario
  val nrOfDictDirs = 10
  val nrOfDictsPerDir = 10
  futures ++= Range(0, nrOfDictDirs).map(dirNr => client.createCi(Directory(s"Environments/dir$dirNr")))
  futures ++= Range(0, nrOfDictDirs).flatMap(dirNr => createNDictionariesIn(nrOfDictsPerDir, s"Environments/dir$dirNr"))

  def createNDictionariesIn(n: Int, dirId: String) = {
    Range(0, n).map(dictNr => client.createCi(Dictionary(s"$dirId/dict$dictNr",
      Map("key1" -> "value1", "jdbc.url" -> "jdbc:oracle:thin:@localhost:1521:ORCL"),
      Map("encryptedKey1" -> "secret", "scott" -> "tiger"))))
  }

  // create environments for runCommandsScenario
  val nrOfEnvs = 10
  val nrOfHostsPerEnv = 10
  futures ++= Range(0, nrOfEnvs).map(envNr => client.createCi(Directory(s"Infrastructure/env${envNr}")))
  futures ++= Range(0, nrOfEnvs).flatMap(envNr => createNHostsIn(nrOfHostsPerEnv, s"Infrastructure/env${envNr}"))

  def createNHostsIn(n: Int, dirId: String) = {
    Range(0, n).map(hostNr => client.createCi(SshHost(s"${dirId}/host${hostNr}", "UNIX", "SCP", s"xldstress${hostNr}.xebialabs.com", "xldstress", "", "/home/xldstress/.ssh/id_rsa")))
  }

  futures ++= Range(0, nrOfEnvs).map(envNr => client.createCi(Environment(s"Environments/env${envNr}", Range(0, nrOfHostsPerEnv).map(hostNr => CiRef(s"Infrastructure/env${envNr}/host${hostNr}", "overthere.SshHost")), Seq())))

  // create package for runCommandsScenario
  futures :+= client.createCi(Application("Applications/cmdapp0")).
    flatMap(_ => client.createCi(DeploymentPackage("Applications/cmdapp0/0", Seq("parallel-by-container")))).
    flatMap(_ => client.createCi(Command("Applications/cmdapp0/0/cmd0", "sleep 10")))

  // create packages for copyFilesScenario
  val nrOfApps = 1
  val nrOfVersionsPerApp = 2
  val nrOfArtifactsPerVersion = 1
  val nrOfMbPerArtifacts = 100
  futures ++= Range(0, nrOfApps).flatMap(appNr => createNVersions(s"filesapp$appNr"))

  def createNVersions(application: String) = {
    Range(0, nrOfVersionsPerApp).map(versionNr => client.generateAndUploadPackage(application, s"$versionNr", nrOfArtifactsPerVersion, nrOfMbPerArtifacts))
  }

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

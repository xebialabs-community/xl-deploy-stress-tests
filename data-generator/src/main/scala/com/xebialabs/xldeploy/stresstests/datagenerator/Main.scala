package com.xebialabs.xldeploy.stresstests.datagenerator

import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigFactory.parseResources
import com.typesafe.scalalogging.LazyLogging
import com.xebialabs.xldeploy.stresstests.datagenerator.client.XldClient
import com.xebialabs.xldeploy.stresstests.datagenerator.domain.{Dictionary, Directory}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Failure

object Main extends App with LazyLogging {

  val config = parseResources("data-generator.conf").withFallback(ConfigFactory.load())

  val client = new XldClient(
    config.getString("xld.data-generator.baseUrl"),
    config.getString("xld.data-generator.username"),
    config.getString("xld.data-generator.password"))

  val createDirectoriesFuture = client.createCi(Directory("Environments/dir1"))
  val createDictionariesFuture = client.createCi(Dictionary("Environments/dir1/dict1"))

  val allResponses = Future.sequence(
    Seq(createDirectoriesFuture, createDictionariesFuture))

  allResponses.andThen {
    case Failure(ex) =>
      logger.error("Could not generate data set: ", ex)
  } andThen {
    case _ =>
      logger.debug("Shutting down the actor system after everything has been done.")
      client.system.shutdown()
      client.system.awaitTermination()
  }
}

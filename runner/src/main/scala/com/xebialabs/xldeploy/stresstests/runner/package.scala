package com.xebialabs.xldeploy.stresstests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import com.xebialabs.xldeploy.stresstests.runner.config.RunnerConfig

import scala.language.{implicitConversions, postfixOps}

package object runner {

  val httpProtocol = http
      .baseURLs(RunnerConfig.input.baseUrls)
      .acceptHeader("application/xml")
      .basicAuth(RunnerConfig.input.username, RunnerConfig.input.password)
      .contentTypeHeader("application/xml").build
}

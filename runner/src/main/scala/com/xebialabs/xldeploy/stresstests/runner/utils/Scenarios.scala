package com.xebialabs.xldeploy.stresstests.runner.utils

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.StringBody
import com.xebialabs.xldeploy.stresstests.runner.chain._
import com.xebialabs.xldeploy.stresstests.runner.config.RunnerConfig._

import scala.language.{implicitConversions, postfixOps}

object Scenarios {

  val readRepositoryScenario = scenario("Read repository").repeat(10, "dictNr") {
    exec(Repository.read("Environments/dir1/dict${dictNr}"))
  }

  val runCommandScenario = scenario("Run command").repeat(1) {
    exec(Deployment.prepareInitialDeployment("Applications/cmdapp1/v1", "Environments/10hosts")).
      exec(Deployment.executeDeployment).
      exec(Deployment.prepareUndeployment("Environments/10hosts/cmdapp1")).
      exec(Deployment.executeDeployment)
  }
}

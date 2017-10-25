package com.xebialabs.xldeploy.stresstests.runner.utils

import java.util.concurrent.atomic.AtomicInteger

import com.xebialabs.xldeploy.stresstests.runner.chain._
import io.gatling.core.Predef._

import scala.language.{implicitConversions, postfixOps}
import scala.util.Random

object Scenarios {

  val readRepositoryScenario = scenario("Read repository").repeat(10, "dictNr") {
    exec(Repository.read("Environments/dir1/dict${dictNr}"))
  }

  val userNumber = new AtomicInteger

  val runCommandsScenario = scenario("Run commands").exec(
    _.set("userNr", userNumber.getAndIncrement())).
    repeat(1) {
      exec(Deployment.prepareInitialDeployment("Applications/cmdapp0/0", "Environments/env${userNr}")).
        exec(Deployment.executeDeployment).
        exec(Deployment.prepareUndeployment("Environments/env${userNr}/cmdapp0")).
        exec(Deployment.executeDeployment)
    }

  def copyFilesScenario(nrOfMb: Int) = scenario("Copy files").exec(
    _.set("userNr", userNumber.getAndIncrement())).
    repeat(1) {
      exec(Deployment.prepareInitialDeployment(s"Applications/files${nrOfMb}app0/0", "Environments/env${userNr}")).
        exec(Deployment.executeDeployment).
        exec(Deployment.prepareUndeployment(s"Environments/env$${userNr}/files${nrOfMb}app0")).
        exec(Deployment.executeDeployment)
    }

  def reportsPageScenario(repeats: Int) = scenario("Retrieve dashboard reports data")
    .exec(_.set("userNr", userNumber.getAndIncrement()))
    .repeat(repeats) {
      DashboardPage.getHtml()
    }

  def renameEnvironmentScenario(repeats: Int) = scenario("Rename deployment")
    .exec(_.set("userNr", userNumber.getAndIncrement()))
    .exec(_.set("rand", Random.nextInt()))
    .repeat(repeats) {
      exec(Environment.create("RenameEnv${rand}-${userNr}")).
        exec(Environment.rename("RenameEnv${rand}-${userNr}", "RenameEnv${rand}-${userNr}-renamed")).
        exec(Environment.rename("RenameEnv${rand}-${userNr}-renamed", "RenameEnv${rand}-${userNr}")).
        exec(Environment.delete("RenameEnv${rand}-${userNr}"))
    }

  def importApplicationScenario(repeats: Int) = scenario("Import application")
    .exec(_.set("userNr", userNumber.getAndIncrement()))
    .repeat(repeats) {
      exec(ImportDar.generateAndUploadPackage("GeneratedApplication", "0.0.1", 5, 2)).
        exec(Application.delete("GeneratedApplication${userNr}"))
    }
}

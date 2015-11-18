package com.xebialabs.xldeploy.stresstests.runner.chain

import java.net.URLEncoder.encode
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

object Deployment {

  def prepareInitialDeployment(versionId: String, environmentId: String) =
    exec(http("1. Prepare initial deployment").
      get(s"/deployment/prepare/initial?version=${encode(versionId, "UTF-8")}&environment=${encode(environmentId, "UTF-8")}").
      check(status.is(200), bodyBytes.saveAs("intialDeployment"))).
      exec(http("2. Map deployeds").
        post("/deployment/prepare/deployeds").
        body(ByteArrayBody(session => session("intialDeployment").validate[Array[Byte]])).
        check(status.is(200), bodyBytes.saveAs("configuredDeployment"))
      )

  def prepareUndeployment(deployedApplicationId: String) =
    exec(http("1. Prepare undeployment").
      get(s"/deployment/prepare/undeploy?deployedApplication=${encode(deployedApplicationId, "UTF-8")}").
      check(status.is(200), bodyBytes.saveAs("configuredDeployment"))
    )

  def executeDeployment =
    exec(http("3. Create deployment task").
      post("/deployment").
      body(ByteArrayBody(session => session("configuredDeployment").validate[Array[Byte]])).
      check(status.is(200), bodyString.saveAs("taskId"))).
      exec(http("4. Start deployment task").
        post("/tasks/v2/${taskId}/start").
        check(status.is(204))).
      exec(http("5. Poll task state").
        get("/tasks/v2/${taskId}").
        check(status.is(200), xpath("/task/@state").saveAs("taskState"))).
      asLongAs(session => session("taskState").as[String] != "EXECUTED") {
        exec { session =>
          println("Task state: " + session("taskState").as[String]);
          session
        }.
          exec(pause(2 seconds)).
          exec(http("5. Poll task state again").
            get("/tasks/v2/${taskId}").
            check(status.is(200), xpath("/task/@state").saveAs("taskState")))
      }.
      exec(http("6. Archive executed task").
        post("/tasks/v2/${taskId}/archive").
        check(status.is(204))
      )

}

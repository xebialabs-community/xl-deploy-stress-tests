package com.xebialabs.xldeploy.stresstests.runner.chain

import java.net.URLEncoder.encode

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

import scala.concurrent.duration._

object Deployment {

  val json_utf_8_header = Map("Accept-Type" -> "application/json","Content-Type" -> "application/json;charset=UTF-8")

  def prepareInitialDeployment(versionId: String, environmentId: String) =
    exec(http("1. Prepare initial deployment").
      get(s"/deployment/prepare/initial").queryParam("version", versionId).queryParam("environment", environmentId)
      check(status.is(200), bodyBytes.saveAs("intialDeployment"))).
      exec(http("2. Map deployeds").
        post("/deployment/prepare/deployeds").
        body(ByteArrayBody(session => session("intialDeployment").validate[Array[Byte]])).
        check(status.is(200), bodyBytes.saveAs("configuredDeployment"))
      )

  def prepareUndeployment(deployedApplicationId: String) =
    exec(http("1. Prepare undeployment").
      get(s"/deployment/prepare/undeploy").queryParam("deployedApplication", deployedApplicationId)
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

  def customerDeployment = exec(http("Before deployment")
    .post("/repository/cis/read")
    .headers(json_utf_8_header)
    .body(StringBody(""" [] """)).asJSON)
    .pause(3)
    .exec(http("Deployment")
    .post("/deployment")
    .headers(json_utf_8_header)
    .body(StringBody(""" {
				"id": "deployment-bd059d09-70f7-43ff-8c67-614688f67042",
				"type": "INITIAL",
				"deploymentGroupIndex": 0,
				"application": {
					"id": "Environments/${userNr}/${userNr}",
					"type": "udm.DeployedApplication",
					"version": "Applications/GeneratedApplication${userNr}/0.0.1",
					"environment": "Environments/${userNr}",
					"deployeds": [],
					"orchestrator": [],
					"optimizePlan": true,
					"boundConfigurationItems": [],
					"unresolvedPlaceholders": {},
					"undeployDependencies": false
				},
				"deployeds": [],
				"deployables": [
					{
						"ci": "Applications/GeneratedApplication${userNr}/0.0.1/collectd-fast-jmx",
						"type": "file.Archive"
					}
				],
				"containers": [
					{
						"ci": "Infrastructure/${userNr}",
						"type": "overthere.LocalHost"
					}
				],
				"requiredDeployments": []} """)).check(status.is(200)))
    .pause(3)
    .exec(http("Prepare deployeds")
    .post("/deployment/prepare/deployeds")
    .headers(json_utf_8_header)
    .body(StringBody(""" {
				"id": "deployment-bd059d09-70f7-43ff-8c67-614688f67042",
				"type": "INITIAL",
				"deploymentGroupIndex": 0,
				"application": {
					"id": "Environments/${userNr}/${userNr}",
					"type": "udm.DeployedApplication",
					"version": "Applications/GeneratedApplication${userNr}/0.0.1",
					"environment": "Environments/${userNr}",
					"deployeds": [],
					"orchestrator": [],
					"optimizePlan": true,
					"boundConfigurationItems": [],
					"unresolvedPlaceholders": {},
					"undeployDependencies": false
				},
				"deployeds": [],
				"deployables": [
					{
						"ci": "Applications/GeneratedApplication${userNr}/0.0.1/collectd-fast-jmx",
						"type": "file.Archive"
					}
				],
				"containers": [
					{
						"ci": "Infrastructure/${userNr}",
						"type": "overthere.LocalHost"
					}
				],
				"requiredDeployments": []} """)).check(status.is(200)))
    .pause(3)
    .exec(http("Validate deployment")
    .post("/deployment/validate")
    .headers(json_utf_8_header)
    .body(StringBody(""" {
				"id": "deployment-bd059d09-70f7-43ff-8c67-614688f67042",
				"type": "INITIAL",
				"deploymentGroupIndex": 0,
				"application": {
					"id": "Environments/${userNr}/${userNr}",
					"type": "udm.DeployedApplication",
					"version": "Applications/GeneratedApplication${userNr}/0.0.1",
					"environment": "Environments/${userNr}",
					"deployeds": [],
					"orchestrator": [],
					"optimizePlan": true,
					"boundConfigurationItems": [],
					"unresolvedPlaceholders": {},
					"undeployDependencies": false
				},
				"deployeds": [],
				"deployables": [
					{
						"ci": "Applications/GeneratedApplication${userNr}/0.0.1/collectd-fast-jmx",
						"type": "file.Archive"
					}
				],
				"containers": [
					{
						"ci": "Infrastructure/${userNr}",
						"type": "overthere.LocalHost"
					}
				],
				"requiredDeployments": []} """)).check(status.is(200)))
    .pause(3)
    .exec(http("After deployment")
    .post("/repository/cis/read")
    .headers(json_utf_8_header)
    .body(StringBody(""" ["Applications/${userNr}/0.0.1"] """)).asJSON)
    .pause(3)
    .exec(http("Check deployment exists")
    .get("/deployment/exists")
    .queryParam("application", "Applications/GeneratedApplication${userNr}")
    .queryParam("environment", "Environments/${userNr}"))

}

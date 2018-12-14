package com.xebialabs.xldeploy.stresstests.runner.chain

import com.xebialabs.xldeploy.stresstests.runner.config.RunnerConfig
import io.gatling.core.Predef._
import io.gatling.http.Predef._

object TaskMonitor {

  val baseUrl = RunnerConfig.input.baseUrls.head

  def getCurrentTasks() =
    exec(http("1. Get deployment and control tasks").
      get("${baseUrl}/tasks/v2/current").
      check(status.is(200)))

  def getTaskInfos() =
    exec(http("2. Get a deployment and control ").
      get("/task/current").
      check(regex("id=\\\"([a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12})\\\"").findAll.saveAs("taskIds"))).
      foreach("${taskIds}", "taskId") {
        exec(http("Get task info")
          .get("/task/${taskId}")
        )}

  def getTaskV2Infos() =
    exec(http("Get a task v2 id")
      .get("/tasks/v2/current")
      .check(regex("id=\\\"([0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12})\\\"").findAll.saveAs("taskTwoIds")))
      .foreach("${taskTwoIds}", "taskTwoId") {
        exec(http("Get task info")
          .get("/tasks/v2/${taskTwoId}"))}

}

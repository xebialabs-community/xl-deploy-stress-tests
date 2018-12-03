package com.xebialabs.xldeploy.stresstests.runner.chain

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object TaskMonitor {

  def getCurrentTasks() =
    exec(http("1. Get deployment and control tasks").
      get("http://xlperf1:4516/deployit/tasks/v2/current").
      check(status.is(200)))

  def getTaskInfos() =
    exec(http("2. Get a deployment and control ").
      get("/task/current").
      check(regex("id=\\\"([0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12})\\\"").findAll.saveAs("taskIds"))).
      foreach("${taskIds}", "taskId") {
        exec(http("Get task info")
          .get("/deployit/task/${taskId}")
        )}

  def getTaskV2Infos() =
    exec(http("Get a task v2 id")
      .get("/deployit/tasks/v2/current")
      .check(regex("id=\\\"([0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12})\\\"").findAll.saveAs("taskTwoIds")))
      .foreach("${taskTwoIds}", "taskTwoId") {
        exec(http("Get task info")
          .get("/deployit/tasks/v2/${taskTwoId}"))}

}

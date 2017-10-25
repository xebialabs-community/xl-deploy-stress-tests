package com.xebialabs.xldeploy.stresstests.runner.chain

import io.gatling.core.Predef._
import io.gatling.http.Predef.{http, status}

object TaskMonitor {

  def getCurrentTasks() =
    exec(http("1. Get deployment and control tasks").
      get("http://xlperf1:4516/deployit/tasks/v2/current").
      check(status.is(200)))

}

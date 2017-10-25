package com.xebialabs.xldeploy.stresstests.runner.chain

import com.xebialabs.xldeploy.stresstests.runner.chain.reporting.Dates.{last30days, lastHalfYear, thisMonth}
import com.xebialabs.xldeploy.stresstests.runner.chain.reporting._
import io.gatling.core.Predef.{exec, _}
import io.gatling.http.Predef.http

object DashboardPage {

  def getHtml() = exec(
    http("Reports Page HTML").get("/")
      .resources(
        DeploymentSuccess().get(thisMonth),
        DeploymentDuration().get(thisMonth),
        DeploymentTrend().get(lastHalfYear),
        DeploymentTrendPercentile().get(lastHalfYear),
        Top5SuccessfulDeployments().get(last30days),
        Top5RetriedDeployments().get(last30days),
        Top5LongestRunningDeployments().get(last30days)
      )
  )
}

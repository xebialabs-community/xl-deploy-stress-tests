package com.xebialabs.xldeploy.stresstests.runner.chain.reporting

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import com.xebialabs.xldeploy.stresstests.runner.chain.reporting.Dates._
import io.gatling.core.Predef._
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder


sealed trait DashboardReports {
  val name: String

  def get(duration: Int): ActionBuilder = get(now.minusDays(1), now)

  def get(fromTo: (ZonedDateTime, ZonedDateTime)): HttpRequestBuilder = get(fromTo._1, fromTo._2)

  def get(from: ZonedDateTime, to: ZonedDateTime): HttpRequestBuilder =
    http(s"dashboard-$name").get(dashboardUrl(name, from, to))

  private[this] val formatter = DateTimeFormatter.ofPattern("dd MMM yy hh:mm:ss z")

  private[this] def dashboardUrl(widgetType: String, begin: ZonedDateTime, end: ZonedDateTime) =
    s"/internal/reports/widgetdata?widgetType=$widgetType&begin=${begin.format(formatter)}&end=${end.format(formatter)}"

}

case class DeploymentSuccess() extends DashboardReports {
  val name: String = "deploymentSuccess"
}

case class DeploymentDuration() extends DashboardReports {
  val name: String = "deploymentDuration"
}

case class DeploymentTrend() extends DashboardReports {
  val name: String = "deploymentTrend"
}

case class DeploymentTrendPercentile() extends DashboardReports {
  val name: String = "deploymentTrendPercentile"
}

case class Top5SuccessfulDeployments() extends DashboardReports {
  val name: String = "top5SuccessfulDeployments"
}

case class Top5RetriedDeployments() extends DashboardReports {
  val name: String = "top5RetriedDeployments"
}

case class Top5LongestRunningDeployments() extends DashboardReports {
  val name: String = "top5LongestRunningDeployments"
}

object DashboardReports {
  def deploymentSuccess: ActionBuilder = DeploymentSuccess().get(thisMonth)
  def deploymentDuration: ActionBuilder = DeploymentDuration().get(thisMonth)
  def deploymentTrend: ActionBuilder = DeploymentTrend().get(lastHalfYear)
  def deploymentTrendPercentile: ActionBuilder = DeploymentTrendPercentile().get(lastHalfYear)
  def top5SuccessfulDeployments: ActionBuilder = Top5SuccessfulDeployments().get(last30days)
  def top5RetriedDeployments: ActionBuilder = Top5RetriedDeployments().get(last30days)
  def top5LongestRunningDeployments: ActionBuilder = Top5LongestRunningDeployments().get(last30days)
}
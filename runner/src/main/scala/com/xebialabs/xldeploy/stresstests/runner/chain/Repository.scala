package com.xebialabs.xldeploy.stresstests.runner.chain

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.StringBody

object Repository {

  def read(id: String) =
    exec(http("Read CI").get(s"/repository/ci/$id"))

  def delete(id: String) =
    exec(http("delete CI").delete(s"/repository/ci/$id"))

  def readCIS() =
    exec(http("Read Multiple Configuration").post("/repository/cis/read").
      body(StringBody(s"""[]""")).asJSON)

  def deleteApplications =
    exec(http("Delete applications").get("/repository/query?parent=Applications&resultsPerPage=-1").
      check(regex("ref=\\\"Applications/([\\w\\d]+)").findAll.saveAs("applications"))).
      foreach("${applications}", "application") {
          exec(http("Delete all applications")
            .delete("/repository/ci/Applications%2F${application}"))}

  def deleteEnvironments =
    exec(http("Delete environments").get("/repository/query?parent=Environments&resultsPerPage=-1").
      check(regex("ref=\\\"Environments/([\\w\\d]+)").findAll.saveAs("environments"))).
      foreach("${environments}", "environment") {
        exec(http("Delete all environments")
          .delete("/repository/ci/Environments%2F${environment}"))}

  def deleteInfrastructures =
    exec(http("Delete infrastructures").get("/repository/query?parent=Infrastructure&resultsPerPage=-1").
      check(regex("ref=\\\"Infrastructure/([\\w\\d]+)").findAll.saveAs("infrastructures"))).
      foreach("${infrastructures}", "infrastructure") {
        exec(http("Delete all environments")
          .delete("/repository/ci/Infrastructure%2F${infrastructure}"))}

}

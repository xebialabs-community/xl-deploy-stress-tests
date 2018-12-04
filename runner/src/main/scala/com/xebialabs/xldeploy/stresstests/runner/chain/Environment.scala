package com.xebialabs.xldeploy.stresstests.runner.chain

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef.{http, jsonPath, status}
import io.gatling.http.request.StringBody

object Environment {

  val json_utf_8_header = Map("Accept-Type" -> "application/json","Content-Type" -> "application/json;charset=UTF-8")

  def list: ChainBuilder =
    exec(http("0. List environments").
      get(s"/repository/query?resultsPerPage=-1&parent=Environments").asJSON.
      check(jsonPath("$[0].ref").saveAs("environmentRef")).
      check(status.is(200)))

  def create(name: String): ChainBuilder =
    exec(http("1. Create environments").
      post(s"/repository/ci/Environments/$name").
      body(StringBody(s"""{"id":"Environments/$name","type":"udm.Environment"}""")).asJSON.
      check(status.is(200)))

  def rename(oldName: String,newName: String): ChainBuilder =
    exec(http("2. Rename environments").
      post(s"/repository/rename/Environments/$oldName?newName=$newName").
      check(status.is(200)))

  def delete(name: String): ChainBuilder =
    exec(http("3. Delete environments").
      delete(s"/repository/ci/Environments/$name").
      check(status.is(204)))

  def checkEnvironment: ChainBuilder =
    exec(http("Check environment exists")
    .post("/repository/candidate-values?namePattern=%25%25&propertyName=members&resultsPerPage=-1")
    .headers(json_utf_8_header)
    .body(StringBody("""{ "id": "Environments/${userNr}", "type":"udm.Environment" }""")).asJSON.
      check(status.is(200)))

  def createCustomerEnvironment: ChainBuilder =
    exec(http("Create environment")
    .post("/repository/ci/Environments/${userNr}")
    .headers(json_utf_8_header)
    .body(StringBody("""{ "id": "Environments/${userNr}", "type":"udm.Environment", "members":["Infrastructure/${userNr}"] }""")).asJSON)

}

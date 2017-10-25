package com.xebialabs.xldeploy.stresstests.runner.chain

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef.{http, jsonPath, status}
import io.gatling.http.request.StringBody

object Environment {

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

}

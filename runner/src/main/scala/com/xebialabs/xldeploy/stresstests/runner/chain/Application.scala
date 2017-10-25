package com.xebialabs.xldeploy.stresstests.runner.chain

import io.gatling.core.Predef._
import io.gatling.http.Predef.{http, status}
import io.gatling.http.request.StringBody

object Application {

  // post
  def create(name: String) =
    exec(http("1. Create application").
      post(s"/repository/ci/Applications%2F$name").
      body(StringBody(s"""{"id":"Applications/$name","type":"udm.Application"}""")).asJSON.
      check(status.is(200)))

  def rename(oldName: String,newName: String) =
    exec(http("2. Rename application").
      post(s"/repository/rename/Applications%2F$oldName?newName=$newName").
      check(status.is(200)))

  def delete(name: String) =
    exec(http("3. Delete application").
      delete(s"/repository/ci/Applications%2F$name").
      check(status.is(204)))

}

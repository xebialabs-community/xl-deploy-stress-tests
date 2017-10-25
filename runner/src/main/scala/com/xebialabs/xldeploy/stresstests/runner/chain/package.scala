package com.xebialabs.xldeploy.stresstests.runner.chain

import io.gatling.core.Predef.{exec, _}
import io.gatling.http.Predef.{http, status}
import io.gatling.http.request.StringBody

object Infrastructure {

  def create(name: String) =
    exec(http("1. Create infrastructure").
      post(s"/repository/ci/Infrastructure/$name").
      body(StringBody(s"""{"id":"Infrastructure/$name","type":"overthere.SmbHost","address":"hfgh","username":"dfg","password":"dfg"}""")).asJSON.
      check(status.is(200)))

  def rename(oldName: String, newName: String) =
    exec(http("2. Rename infrastructure").
      post(s"/repository/rename/Infrastructure/$oldName?newName=$newName").
      check(status.is(200)))

  def delete(name: String) =
    exec(http("3. Delete infrastructure").
      delete(s"/repository/ci/Infrastructure/$name").
      check(status.is(204)))

}

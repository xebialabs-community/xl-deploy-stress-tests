package com.xebialabs.xldeploy.stresstests.runner

import io.gatling.core.Predef.{exec, _}
import io.gatling.http.Predef.{http, status}
import io.gatling.http.request.StringBody

object Configuration {

  def create(name: String) =
    exec(http("1. Create Configuration").
      post(s"/repository/ci/Configuration/$name").
      body(StringBody(s"""{"id":"Configuration/$name","type":"mail.SmtpServer","host":"SMTPSERVER1","fromAddress":"SMTPSERVER1"}""")).asJSON.
      check(status.is(200)))

  def rename(oldName: String, newName: String) =
    exec(http("2. Rename Configuration").
      post(s"/repository/rename/Configuration/$oldName?newName=$newName").
      check(status.is(200)))

  def delete(name: String) =
    exec(http("3. Delete Configuration").
      delete(s"/repository/ci/Configuration/$name").
      check(status.is(204)))

}

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
      body(StringBody(s"""{[]}""")).asJSON.
      check(status.is(200)))

}

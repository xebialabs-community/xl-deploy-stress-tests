package com.xebialabs.xldeploy.stresstests.runner.chain

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object Repository {

  def read(id: String) =
    exec(http("Read CI").get("/repository/ci/" + id))

}

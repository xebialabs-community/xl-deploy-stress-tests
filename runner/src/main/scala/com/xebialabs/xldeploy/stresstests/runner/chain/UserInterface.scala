package com.xebialabs.xldeploy.stresstests.runner.chain

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.structure.ChainBuilder

import scala.util.Random

object UserInterface {

  def randomAlphanum = Random.alphanumeric.take(20).mkString

  def load: ChainBuilder =
    exec(http("1. Load UI elements").
      get(s"http://localhost:4516/ciExplorerDist/libs/js/polyfills-f608a712cbf85c1ecd2a.js?$randomAlphanum").
      check(status.is(200)))

}

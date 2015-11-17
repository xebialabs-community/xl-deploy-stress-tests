package com.xebialabs.xldeploy.stresstests.runner

import io.gatling.core.Predef._
import com.xebialabs.xldeploy.stresstests.runner.config.RunnerConfig
import com.xebialabs.xldeploy.stresstests.runner.utils.Scenarios._

import scala.language.{implicitConversions, postfixOps}

class ReadRepositorySimulation extends SimulationBase(readRepositoryScenario)

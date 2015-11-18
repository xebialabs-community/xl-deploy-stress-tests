package com.xebialabs.xldeploy.stresstests.runner

import com.xebialabs.xldeploy.stresstests.runner.utils.Scenarios._

import scala.language.{implicitConversions, postfixOps}

class ReadRepositorySimulation extends SimulationBase(readRepositoryScenario)

class RunCommandSimulation extends SimulationBase(runCommandScenario)
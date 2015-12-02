package com.xebialabs.xldeploy.stresstests.runner

import com.xebialabs.xldeploy.stresstests.runner.utils.Scenarios._

import scala.language.{implicitConversions, postfixOps}

class ReadRepositorySimulation extends SimulationBase(readRepositoryScenario)

class RunCommandsSimulation extends SimulationBase(runCommandsScenario)

class CopyFiles100Simulation extends SimulationBase(copyFilesScenario(100))

class CopyFiles200Simulation extends SimulationBase(copyFilesScenario(200))

class CopyFiles400Simulation extends SimulationBase(copyFilesScenario(400))

class CopyFiles800Simulation extends SimulationBase(copyFilesScenario(800))

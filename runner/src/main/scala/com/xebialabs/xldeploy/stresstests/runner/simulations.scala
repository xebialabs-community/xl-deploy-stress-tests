package com.xebialabs.xldeploy.stresstests.runner

import com.xebialabs.xldeploy.stresstests.runner.config.RunnerConfig.simulations
import com.xebialabs.xldeploy.stresstests.runner.utils.Scenarios._
import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation

import scala.language.{implicitConversions, postfixOps}

class ReadRepositorySimulation extends SimulationBase(readRepositoryScenario)

class RunCommandsSimulation extends SimulationBase(runCommandsScenario)

class CopyFiles100Simulation extends SimulationBase(copyFilesScenario(100))

class CopyFiles200Simulation extends SimulationBase(copyFilesScenario(200))

class CopyFiles400Simulation extends SimulationBase(copyFilesScenario(400))

class CopyFiles800Simulation extends SimulationBase(copyFilesScenario(800))

class DashboardReportSimulation extends SimulationBase(reportsPageScenario(1))

class CustomerSimulation extends SimulationBase(customerSimulationScenario(1))

class LatencyOnRenameSimulation extends Simulation {

  private val rampUpPeriod = simulations.realistic.rampUpPeriod
  private val repeats = simulations.realistic.repeats

  setUp(
    reportsPageScenario(repeats).inject(rampUsers(5) over rampUpPeriod),
    importApplicationScenario(repeats).inject(rampUsers(100) over rampUpPeriod),
    renameEnvironmentScenario(repeats).inject(rampUsers(250) over rampUpPeriod)
  ).protocols(httpProtocol)

}

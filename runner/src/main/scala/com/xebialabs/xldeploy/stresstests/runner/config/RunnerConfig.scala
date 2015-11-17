package com.xebialabs.xldeploy.stresstests.runner.config

import java.util.concurrent.TimeUnit.MILLISECONDS

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.duration.{FiniteDuration, Duration}

/**
 * When adding a new configuration option don't forget to check that it has the same path at `com.xebialabs.xldeploy.stresstests.runner.conf`.
 * If you add a duration, use [[RunnerConfig.duration()]] for accessing it.
 */
object RunnerConfig extends LazyLogging {

  private val CONFIG_OBJECT_PATH = "xl.com.xebialabs.xldeploy.stresstests.runner"

  lazy private val rootConfig = ConfigFactory.load("runner.conf")

  lazy private val runnerConfig = rootConfig.getConfig(CONFIG_OBJECT_PATH)

  private val durationDilation = runnerConfig.getDouble("durationDilation")

  /**
   * This object contains public, user-facing config parameters.
   */
  object input {

    val users = rootConfig.getInt("xl.com.xebialabs.xldeploy.stresstests.runner.input.users")

    def baseUrls: List[String] = runnerConfig.getString("input.baseUrl").split(",").toList

    val username = runnerConfig.getString("input.username")

    val password = runnerConfig.getString("input.password")

  }

  object simulations {

    val postWarmUpPause =  duration("simulations.postWarmUpPause")

    val rampUpPeriod = duration("simulations.rampUpPeriod")

    object realistic {

      val rampUpPeriod = duration("simulations.realistic.rampUpPeriod")

      val repeats = runnerConfig.getInt("simulations.realistic.repeats")

    }
  }


  // Helpers

  /**
   * Always use this method to calculate duration. It will also take into account [[durationDilation]].
   */
  private def duration(path: String): FiniteDuration = {

    val duration = Duration(runnerConfig.getDuration(path, MILLISECONDS), MILLISECONDS)

    duration * durationDilation match {
      case fd: FiniteDuration => fd
      case _: Duration =>
        logger.warn(s"Using dilation factor $durationDilation resulted in infinite duration for $path. Falling back to non-dilated value: $duration.")
        duration
    }
  }
}

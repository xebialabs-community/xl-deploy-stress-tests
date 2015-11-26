package com.xebialabs.xldeploy.stresstests.datagenerator

import scala.language.implicitConversions

package object domain {

  trait Ci {
    def id: String
    def `type`: String
  }

  case class CiRef(id: String, `type`: String) extends Ci

  case class Application(id: String, `type`: String = "udm.Application") extends Ci

  case class Command(id: String, commandLine: String, `type`: String = "cmd.Command") extends Ci

  case class DeploymentPackage(id: String, orchestrator: Seq[String], `type`: String = "udm.DeploymentPackage") extends Ci

  case class Directory(id: String, `type`: String = "core.Directory") extends Ci

  case class Dictionary(id: String, entries: Map[String, String], encryptedEntries: Map[String, String], `type`: String = "udm.Dictionary") extends Ci

  case class Environment(id: String, members: Seq[CiRef], dictionaries: Seq[CiRef], `type`: String = "udm.Environment") extends Ci

  case class SshHost(id: String, os: String, connectionType: String, address: String, username: String, password: String, privateKeyFile: String, `type`: String = "overthere.SshHost") extends Ci

}

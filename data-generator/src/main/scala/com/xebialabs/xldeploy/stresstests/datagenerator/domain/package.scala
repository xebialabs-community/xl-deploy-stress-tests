package com.xebialabs.xldeploy.stresstests.datagenerator

import scala.language.implicitConversions

package object domain {

  trait Ci {
    def id: String
    def `type`: String
  }

  case class Dictionary(id: String, `type`: String = "udm.Dictionary") extends Ci

  case class Directory(id: String, `type`: String = "core.Directory") extends Ci

}

package com.xebialabs.xldeploy.stresstests.datagenerator.json

import com.xebialabs.xldeploy.stresstests.datagenerator.domain._
import spray.json._

trait XldJsonProtocol extends DefaultJsonProtocol with AdditionalFormats with ZonedDateTimeProtocol {
  this: ProductFormatsInstances =>

  implicit val ciRefFormat = jsonFormat2(CiRef.apply)
  implicit val applicationFormat = jsonFormat2(Application.apply)
  implicit val commandFormat = jsonFormat3(Command.apply)
  implicit val deploymentPackageFormat = jsonFormat2(DeploymentPackage.apply)
  implicit val dictionaryFormat = jsonFormat4(Dictionary.apply)
  implicit val directoryFormat = jsonFormat2(Directory.apply)
  implicit val environmentFormat = jsonFormat4(Environment.apply)
  implicit val sshHostFormat = jsonFormat7(SshHost.apply)

  implicit object CiProtocol extends RootJsonFormat[Ci] {
    def read(json: JsValue): Ci = {
      deserializationError("Read is not implemented")
    }

    def write(ci: Ci): JsValue = {
      ci match {
        case ci: Application => ci.toJson
        case ci: Command => ci.toJson
        case ci: DeploymentPackage => ci.toJson
        case ci: Dictionary => ci.toJson
        case ci: Directory => ci.toJson
        case ci: Environment => ci.toJson
        case ci: SshHost => ci.toJson
        case _ => serializationError(s"Undefined CI type ${ci.getClass}")
      }
    }
  }

}

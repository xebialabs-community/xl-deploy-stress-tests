package com.xebialabs.xldeploy.stresstests.datagenerator.json

import com.xebialabs.xldeploy.stresstests.datagenerator.domain._
import spray.json._

trait XldJsonProtocol extends DefaultJsonProtocol with AdditionalFormats with ZonedDateTimeProtocol {
  this: ProductFormatsInstances =>

  implicit val directoryFormat = jsonFormat2(Directory.apply)
  implicit val dictionaryFormat = jsonFormat2(Dictionary.apply)


  implicit object CiProtocol extends RootJsonFormat[Ci] {
    def read(json: JsValue): Ci = {
      deserializationError("Read is not implemented")
    }

    def write(ci: Ci): JsValue = {
      ci match {
        case ci: Directory => ci.toJson
        case ci: Dictionary => ci.toJson
        case _ => serializationError(s"Undefined CI type ${ci.getClass}")
      }
    }
  }
}

package com.xebialabs.xldeploy.stresstests.runner.chain

import java.io.ByteArrayOutputStream
import java.util.zip.{ZipEntry, ZipOutputStream}

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.util.Random

object ImportDar {

  def generateAndUploadPackage(applicationName: String, version: String, nrOfArtifacts: Int, nrOfMbPerArtifact: Int) = {
    http(s"import dar")
      .post(s"/package/upload/$applicationName$${userNr}-$version.dar").asMultipartForm
      .bodyPart(ByteArrayBodyPart("fileData", session => getByteArray(applicationName+session.get("userNr").as[String],version,nrOfArtifacts,nrOfMbPerArtifact)).
          contentType("application/octet-stream")
      )
  }

  def getByteArray(applicationName: String, version: String, nrOfArtifacts: Int, nrOfMbPerArtifact: Int) = {
    var bos = new ByteArrayOutputStream()
    val darStream = new ZipOutputStream(bos)
    val manifest = <udm.DeploymentPackage application={applicationName} version={version}>
      <orchestrator>
        <value>parallel-by-container</value>
      </orchestrator>
      <deployables>
        {for (a <- 0 until nrOfArtifacts) yield
        <file.File name={s"artifact-$applicationName-$version-$a"} file={s"artifact-$applicationName-$version-$a.bin"}>
          <targetPath>/home/xldstress</targetPath>
        </file.File>}
      </deployables>
    </udm.DeploymentPackage>
    darStream.putNextEntry(new ZipEntry("deployit-manifest.xml"))
    darStream.write(manifest.toString().getBytes("UTF-8"))
    darStream.closeEntry()

    for (a <- 0 until nrOfArtifacts) {
      darStream.putNextEntry(new ZipEntry(s"artifact-$applicationName-$version-$a.bin"))
      for (mb <- 1 to nrOfMbPerArtifact) {
        val randomBytes = new Array[Byte](1048576)
        Random.nextBytes(randomBytes)
        darStream.write(randomBytes)
      }
      darStream.closeEntry()
    }

    darStream.close()
    bos.toByteArray
  }
}
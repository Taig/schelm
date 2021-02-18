import java.util.regex.Pattern

import sbt.Def.Classpath
import sbt.Keys.artifact
import sbt._

object ScalaJsJvmStubsGenerator {
  def removeImports(value: String): String = {
    Pattern
      .compile("^import scala\\.scalajs\\.js\\.\\w.*\n", Pattern.MULTILINE)
      .matcher(value)
      .replaceAll("")
  }

  def removeAnnotations(value: String): String =
    Pattern
      .compile("@(JSGlobal|js\\.native|scala\\.scalajs\\.js\\.annotation\\.JSBracketAccess)\n", Pattern.MULTILINE)
      .matcher(value)
      .replaceAll("")

  def removeTypes(value: String): String =
    value
      .replace("js.Any", "Any")
      .replaceAll("js.Function(\\d+)", "Function$1")
      .replace("js.Object", "Object")
      .replace("js.Dynamic", "Dynamic")
      .replace("js.Array", "Array")

  def removeJsNativeCalls(value: String): String =
    value
      .replace("js.native", "???")
      .replace("js.undefined", "???")

  def removeWeirdTypes(value: String): String =
    value
      .replace("with IDBEnvironment", "")
      .replace("with GetSVGDocument", "")

  def removeFailingCode(value: String): String =
    Pattern
      .compile("= \\{.*?\\}", Pattern.MULTILINE | Pattern.DOTALL)
      .matcher(value)
      .replaceAll("= ???")

  def removeScalajs(value: String): String =
    (removeImports _)
      .andThen(removeAnnotations)
      .andThen(removeTypes)
      .andThen(removeJsNativeCalls)
      .andThen(removeWeirdTypes)
      .andThen(removeFailingCode)
      .apply(value)

  def apply(classpath: Classpath): List[String] = {
    val path = classpath
      .find { file => file.metadata.get(artifact.key).exists(_.name.startsWith("scalajs-dom")) }
      .map(jar => file(jar.data.getAbsolutePath.replaceAll("\\.jar$", "") + "-sources.jar"))

    path match {
      case Some(path) =>
        IO.withTemporaryDirectory { temp =>
          IO.unzip(path, temp)

          val stdlib =
            s"""package scala.scalajs
               |
               |package object js {
               |  type UndefOr[+A]
               |  type Promise[+A]
               |  type Dictionary[+A]
               |  type |[A, B]
               |  type Dynamic = scala.Dynamic
               |}""".stripMargin

          val domLib =
            s"""package org.scalajs.dom
               |
               |package object raw {
               |  type CSSStyleDeclaration
               |  type CSSStyleSheet
               |  type ArrayBuffer
               |}""".stripMargin

          val lib = removeScalajs(IO.read(temp / "org" / "scalajs" / "dom" / "raw" / "lib.scala"))
          val html = removeScalajs(IO.read(temp / "org" / "scalajs" / "dom" / "raw" / "Html.scala"))

          List(stdlib, domLib, lib, html)
        }
      case None => List()
    }
  }
}

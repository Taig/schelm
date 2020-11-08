import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport.scalaJSLinkerConfig
import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}
import sbtcrossproject.CrossProject

val Version = new {
  val Cats = "2.2.0"
  val CatsEffect = "3.0.0-M2"
  val CatsScalacheck = "0.3.0"
  val Color = "0.2.3"
  val DisciplineMunit = "1.0.1"
  val Fs2 = "3.0.0-M2"
  val Jsoup = "1.13.1"
  val Munit = "0.7.16"
  val ScalaCollectionCompat = "2.2.0"
  val ScalajsDom = "1.1.0"
}

noPublishSettings

ThisBuild / testFrameworks += new TestFramework("munit.Framework")

lazy val core: CrossProject = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Full)
  .in(file("modules/core"))
  .settings(sonatypePublishSettings)
  .settings(
    libraryDependencies ++=
      "org.typelevel" %%% "cats-effect" % Version.CatsEffect ::
        "org.typelevel" %%% "alleycats-core" % Version.Cats ::
        "co.fs2" %%% "fs2-core" % Version.Fs2 ::
        "io.chrisdavenport" %%% "cats-scalacheck" % Version.CatsScalacheck % "test" ::
        "org.scalameta" %%% "munit" % Version.Munit % "test" ::
        "org.typelevel" %%% "cats-testkit" % Version.Cats % "test" ::
        "org.typelevel" %%% "discipline-munit" % Version.DisciplineMunit % "test" ::
        Nil,
    name := "schelm-core"
  )
  .jvmSettings(
    libraryDependencies ++=
      "org.jsoup" % "jsoup" % Version.Jsoup ::
        "org.scala-lang.modules" %% "scala-collection-compat" % Version.ScalaCollectionCompat ::
        Nil,
    scalacOptions += "-Wconf:src=src_managed/.*:silent",
    Compile / sourceGenerators += Def.taskDyn {
      Def.task {
        val classpath = (core.js / Compile / dependencyClasspath).value
        val sources = ScalaJsJvmStubsGenerator(classpath)
        val target = (Compile / sourceManaged).value
        sources.zipWithIndex.map {
          case (source, index) =>
            val file = target / s"scalajs-stub-$index.scala"
            IO.write(file, source)
            file
        }
      }
    }
  )
  .jsSettings(
    libraryDependencies ++=
      "org.scala-js" %%% "scalajs-dom" % Version.ScalajsDom ::
        Nil,
    scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule))
  )

lazy val css = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Full)
  .in(file("modules/css"))
  .settings(sonatypePublishSettings)
  .settings(
    name := "schelm-css"
  )
  .dependsOn(core)

lazy val redux = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Full)
  .in(file("modules/redux"))
  .settings(sonatypePublishSettings)
  .settings(
    name := "schelm-redux"
  )
  .dependsOn(core)

lazy val dsl = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Full)
  .in(file("modules/dsl"))
  .settings(sonatypePublishSettings)
  .settings(
    libraryDependencies ++=
      "io.taig" %%% "color-core" % Version.Color ::
        Nil,
    name := "schelm-dsl",
    Compile / sourceGenerators += Def.task {
      val html = (Compile / sourceManaged).value / "html.scala"
      IO.write(html, HtmlGenerator())
      List(html)
    }
  )
  .dependsOn(css, redux)

lazy val material = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Full)
  .in(file("modules/material"))
  .settings(sonatypePublishSettings)
  .settings(
    name := "schelm-material"
  )
  .dependsOn(dsl)

lazy val documentation = project
  .enablePlugins(ScalaJSPlugin)
  .in(file("modules/documentation"))
  .settings(noPublishSettings)
  .settings(
    name := "schelm-documentation",
    packageSite := {
      import scala.sys.process._
      val target = crossTarget.value / "site"
      val resources = (Compile / resourceDirectory).value
      val index = resources / "index.html" -> target / "index.html"
      val javascript = (Compile / fastOptJS).value.data -> target / "asset" / "javascript" / "main.js"
      IO.copy(List(index, javascript))
      s"open ${index._2}".!
      target
    },
    scalaJSUseMainModuleInitializer := true
  )
  .dependsOn(material.js)

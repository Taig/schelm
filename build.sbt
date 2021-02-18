import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}
import sbtcrossproject.CrossProject

noPublishSettings

ThisBuild / scalacOptions ++= "-Ypatmat-exhaust-depth" :: "off" :: Nil
ThisBuild / scalaVersion := "2.13.5"

val Version = new {
  val CatsEffect = "3.0.0-RC2"
  val Color = "0.4.1"
  val Fs2 = "3.0.0-M9"
  val Jsoup = "1.13.1"
  val ScalajsDom = "1.1.0"
}

lazy val dom: CrossProject = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Full)
  .in(file("modules/dom"))
  .settings(
    name := "schelm-dom",
    scalacOptions += "-Wconf:src=src_managed/.*:silent",
    Compile / sourceGenerators += Def.taskDyn {
      Def.task {
        val classpath = (dom.js / Compile / dependencyClasspath).value
        val sources = ScalaJsJvmStubsGenerator(classpath)
        val target = (Compile / sourceManaged).value
        sources.zipWithIndex.map { case (source, index) =>
          val file = target / s"scalajs-stub-$index.scala"
          println(file)
          IO.write(file, source)
          file
        }
      }
    }
  )
  .jsSettings(
    libraryDependencies ++=
      "org.scala-js" %%% "scalajs-dom" % Version.ScalajsDom ::
        Nil
  )

lazy val core = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Full)
  .in(file("modules/core"))
  .settings(sonatypePublishSettings)
  .settings(
    libraryDependencies ++=
      "org.typelevel" %%% "cats-effect" % Version.CatsEffect ::
        "co.fs2" %%% "fs2-core" % Version.Fs2 ::
        Nil,
    name := "schelm-core"
  )
  .jvmSettings(
    libraryDependencies ++=
      "org.jsoup" % "jsoup" % Version.Jsoup ::
        Nil
  )
  .dependsOn(dom)

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
  .dependsOn(core, css, redux)

lazy val ui = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Full)
  .in(file("modules/ui"))
  .settings(sonatypePublishSettings)
  .settings(
    name := "schelm-ui"
  )
  .dependsOn(dsl)

lazy val documentation = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Full)
  .in(file("modules/documentation"))
  .settings(noPublishSettings)
  .settings(
    name := "schelm-documentation",
    scalaJSUseMainModuleInitializer := true
  )
  .dependsOn(ui)

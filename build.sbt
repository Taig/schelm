import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

lazy val root = project
  .in(file("."))
  .settings(noPublishSettings)
  .settings(
    name := "schelm"
  )
  .aggregate(
    coreJVM,
    coreJS,
    cssJVM,
    cssJS,
    dslJVM,
    dslJS,
    playgroundJVM,
    playgroundJS
  )

lazy val core = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .settings(sonatypePublishSettings)
  .settings(
    libraryDependencies ++=
      "org.typelevel" %%% "cats-effect" % "2.0.0" ::
        "co.fs2" %%% "fs2-core" % "2.1.0" ::
        Nil,
    name := "schelm-core"
  )
  .jvmSettings(
    libraryDependencies ++=
      "org.jsoup" % "jsoup" % "1.12.1" ::
        "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2" ::
        Nil
  )
  .jsSettings(
    libraryDependencies ++=
      "org.scala-js" %%% "scalajs-dom" % "0.9.8" ::
        Nil
  )

lazy val coreJVM = core.jvm

lazy val coreJS = core.js

lazy val css = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .settings(sonatypePublishSettings)
  .settings(
    name := "schelm-css",
    Compile / sourceGenerators += Def.task {
      val source = (baseDirectory in LocalRootProject).value / "normalize.css" / "normalize.css"
      val target = (sourceManaged in Compile).value / "normalize.scala"
      NormalizeCssGenerator("io.taig.schelm.css", source, target)
      Seq(target)
    }
  )
  .dependsOn(core)

lazy val cssJVM = css.jvm

lazy val cssJS = css.js

lazy val dsl = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .settings(sonatypePublishSettings)
  .settings(
    name := "schelm-dsl"
  )
  .dependsOn(css)

lazy val dslJVM = dsl.jvm

lazy val dslJS = dsl.js

lazy val website = project
  .enablePlugins(MicrositesPlugin)
  .settings(micrositeSettings)
  .settings(
    mdocVariables ++= {
      val format: String => String =
        version => s"`${version.replaceAll("\\.\\d+$", "")}`"

      Map(
        "MODULE_CORE" -> (coreJVM / normalizedName).value,
        "MODULE_CSS" -> (cssJVM / normalizedName).value,
        "MODULE_DSL" -> (dslJVM / normalizedName).value,
        "ORGANIZATION" -> organization.value,
        "VERSION" -> version.value,
        "SCALA_VERSIONS" -> crossScalaVersions.value.map(format).mkString(", "),
        "SCALAJS_VERSION" -> format(scalaJSVersion)
      )
    },
    name := "schelm-website",
    micrositeBaseUrl := "",
    micrositeDescription := "The Elm architecture on top of cats-effect and fs2",
    micrositeHomepage := "https://schelm.taig.io",
    micrositeName := "Schelm",
    micrositeUrl := micrositeHomepage.value
  )
  .dependsOn(dslJVM)

lazy val playground = crossProject(JVMPlatform, JSPlatform)
  .settings(noPublishSettings)
  .settings(
    name := "schelm-playground",
    scalaJSUseMainModuleInitializer := true
  )
  .dependsOn(dsl)

lazy val playgroundJVM = playground.jvm

lazy val playgroundJS = playground.js

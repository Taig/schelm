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
  .settings(sonatypePublishSettings)
  .settings(
    libraryDependencies ++=
      "org.typelevel" %%% "cats-effect" % "2.0.0-M4" ::
        "co.fs2" %%% "fs2-core" % "1.1.0-M1" ::
        Nil,
    name := "schelm-core"
  )
  .jvmSettings(
    libraryDependencies ++=
      "org.jsoup" % "jsoup" % "1.12.1" ::
        Nil
  )
  .jsSettings(
    libraryDependencies ++=
      "org.scala-js" %%% "scalajs-dom" % "0.9.7" ::
        Nil
  )

lazy val coreJVM = core.jvm

lazy val coreJS = core.js

lazy val css = crossProject(JVMPlatform, JSPlatform)
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
  .settings(noPublishSettings)
  .settings(
    mdocVariables ++= Map(
      "MODULE_CORE" -> (normalizedName in coreJVM).value,
      "MODULE_CSS" -> (normalizedName in cssJVM).value,
      "MODULE_DSL" -> (normalizedName in dslJVM).value,
      "ORGANIZATION" -> organization.value,
      "VERSION" -> version.value
    ),
    name := "schelm-website",
    micrositeCompilingDocsTool := WithMdoc,
    micrositeDescription := "The Elm architecture on top of cats-effect and fs2",
    micrositeImgDirectory := mdocIn.value / "image",
    micrositeName := "Schelm",
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

addCommandAlias("docs", ";++ 2.12.8 website/makeMicrosite")

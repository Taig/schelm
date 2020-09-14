import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

val CatsEffectVersion = "2.2.0"
val Fs2Version = "2.4.4"
val JsoupVersion = "1.13.1"
val ScalaCollectionCompatVersion = "2.2.0"
val ScalajsDomVersion = "1.1.0"
val ShapelessVersion = "2.3.3"

noPublishSettings

lazy val core = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Full)
  .in(file("modules/core"))
  .settings(sonatypePublishSettings)
  .settings(
    libraryDependencies ++=
      "org.typelevel" %%% "cats-effect" % CatsEffectVersion ::
        "co.fs2" %%% "fs2-core" % Fs2Version ::
        Nil,
    name := "core"
  )

lazy val domJsoup = project
  .in(file("modules/dom-jsoup"))
  .settings(
    libraryDependencies ++=
      "org.jsoup" % "jsoup" % JsoupVersion ::
        "org.scala-lang.modules" %% "scala-collection-compat" % ScalaCollectionCompatVersion ::
        Nil,
    name := "dom-jsoup"
  )
  .dependsOn(core.jvm)

lazy val domBrowser = project
  .enablePlugins(ScalaJSPlugin)
  .in(file("modules/dom-browser"))
  .settings(
    libraryDependencies ++=
      "org.scala-js" %%% "scalajs-dom" % ScalajsDomVersion ::
        Nil,
    name := "dom-browser"
  )
  .dependsOn(core.js)

lazy val css = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Full)
  .in(file("modules/css"))
  .settings(sonatypePublishSettings)
  .settings(
    name := "css"
  )
  .dependsOn(core)

lazy val dsl = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Full)
  .in(file("modules/dsl"))
  .settings(sonatypePublishSettings)
  .settings(
    name := "dsl"
  )
  .dependsOn(css)

lazy val mdc = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Full)
  .in(file("modules/mdc"))
  .settings(sonatypePublishSettings)
  .settings(
    name := "mdc"
  )
  .dependsOn(dsl)

lazy val playground = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Full)
  .in(file("modules/playground"))
  .settings(noPublishSettings)
  .settings(
    name := "playground",
    scalaJSUseMainModuleInitializer := true
  )
  .dependsOn(mdc)

import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

val CatsEffectVersion = "2.2.0"
val Fs2Version = "2.4.4"
val JsoupVersion = "1.13.1"
val ScalaCollectionCompatVersion = "2.1.6"
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

lazy val domBrowser = project
  .in(file("modules/dom-browser"))
  .enablePlugins(ScalaJSPlugin)
  .settings(sonatypePublishSettings)
  .settings(
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++=
      "org.scala-js" %%% "scalajs-dom" % ScalajsDomVersion ::
        Nil,
    name := "dom-browser"
  )
  .dependsOn(core.js)

lazy val domJsoup = project
  .in(file("modules/dom-jsoup"))
  .settings(sonatypePublishSettings)
  .settings(
    libraryDependencies ++=
      "org.jsoup" % "jsoup" % JsoupVersion ::
        "org.scala-lang.modules" %% "scala-collection-compat" % ScalaCollectionCompatVersion ::
        Nil,
    name := "dom-jsoup"
  )
  .dependsOn(core.jvm)

lazy val css = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Full)
  .in(file("modules/css"))
  .settings(sonatypePublishSettings)
  .dependsOn(core)

lazy val dsl = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Full)
  .in(file("modules/dsl"))
  .settings(sonatypePublishSettings)
  .dependsOn(css)

lazy val mdc = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Full)
  .in(file("modules/mdc"))
  .settings(sonatypePublishSettings)
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

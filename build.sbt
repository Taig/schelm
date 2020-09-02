import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

val CatsEffectVersion = "2.1.4"
val Fs2Version = "2.4.4"
val JsoupVersion = "1.13.1"
val ScalaCollectionCompatVersion = "2.1.6"
val ScalajsDomVersion = "1.1.0"

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
  .jvmSettings(
    libraryDependencies ++=
      "org.jsoup" % "jsoup" % JsoupVersion ::
        "org.scala-lang.modules" %% "scala-collection-compat" % ScalaCollectionCompatVersion ::
        Nil
  )
  .jsSettings(
    libraryDependencies ++=
      "org.scala-js" %%% "scalajs-dom" % ScalajsDomVersion ::
        Nil
  )

lazy val css = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Full)
  .in(file("modules/css"))
  .settings(sonatypePublishSettings)
  .dependsOn(core)

lazy val playground = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Full)
  .in(file("modules/playground"))
  .settings(noPublishSettings)
  .settings(
    name := "playground",
    scalaJSUseMainModuleInitializer := true
  )
  .dependsOn(css)

addSbtPlugin("io.taig" % "sbt-houserules" % "0.0.8")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.29")

addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "0.6.1")

libraryDependencies ++=
  "com.helger" % "ph-css" % "6.2.0" ::
    Nil

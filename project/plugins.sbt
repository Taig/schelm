addSbtPlugin("io.taig" % "sbt-houserules" % "0.0.1-SNAPSHOT")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.28")

addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "0.6.1")

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++=
  "com.helger" % "ph-css" % "6.2.0" ::
    Nil

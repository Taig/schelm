package io.taig.schelm.playground

import cats.effect.{ExitCode, IO, IOApp, Resource}
import cats.implicits._
import io.taig.schelm.dsl.interpreter.DslWidgetSchelm
import io.taig.schelm.interpreter.BrowserDom
import io.taig.schelm.mdc.MdcTheme

object JsPlaygroundApp extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val dom = BrowserDom[IO]

    (for {
      root <- Resource.liftF(dom.getElementById("main").flatMap(_.liftTo[IO](new IllegalStateException)))
      schelm <- Resource.liftF(DslWidgetSchelm.empty[IO, MdcTheme](dom)(root))
      _ <- schelm.start(PlaygroundApp.render[IO]("yolo"))
    } yield ExitCode.Success).use(_ => IO.never)
  }
}

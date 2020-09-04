package io.taig.schelm.playground

import cats.effect.{ExitCode, IO, IOApp}
import io.taig.schelm.interpreter.JvmHtmlSchelm

object JvmPlaygroundApp extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val schelm = JvmHtmlSchelm.default[IO, Event]
    schelm.markup(PlaygroundApp.render(PlaygroundApp.Initial))
      .flatMap(markup => IO(println(markup)))
      .as(ExitCode.Success)
  }
}

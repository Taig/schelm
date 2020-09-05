package io.taig.schelm.playground

import cats.effect.{ExitCode, IO, IOApp}
import io.taig.schelm.interpreter.JsHtmlSchelm
import org.scalajs.dom.document

object JsPlaygroundApp extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    JsHtmlSchelm
      .default[IO, Event](document.getElementById("main"))
      .flatMap { schelm => schelm.start(PlaygroundApp.Initial, PlaygroundApp.render, new MyHandler[IO]) }
      .as(ExitCode.Success)
}

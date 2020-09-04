package io.taig.schelm.playground

import cats.effect.{ExitCode, IO, IOApp}
import io.taig.schelm.interpreter.JsHtmlSchelm
import org.scalajs.dom.document

object Playground extends IOApp {
  final case class State(label: String)

  override def run(args: List[String]): IO[ExitCode] = {
    JsHtmlSchelm
      .default[IO, Shared.Event]
      .flatMap { schelm =>
        schelm.start[State](
          document.getElementById("main"),
          State(label = "foobar"),
          state => Shared.html(state.label),
          (state, _) => state.copy(label = "yolo")
        )
      }
      .as(ExitCode.Success)
  }
}

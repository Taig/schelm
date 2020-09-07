package io.taig.schelm.playground

import cats.effect.{ExitCode, IO, IOApp}
import io.taig.schelm.css.data.JsCssSchelm
import org.scalajs.dom.document

object JsPlaygroundApp extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    JsCssSchelm
      .default[IO, Event](document.getElementById("main"))
      .flatMap(schelm => schelm.start(PlaygroundApp.Initial, PlaygroundApp.renderCss, new MyHandler[IO]))
      .as(ExitCode.Success)

  //    JsHtmlSchelm
//      .default[IO, Event](document.getElementById("main"))
//      .flatMap { schelm => schelm.start(PlaygroundApp.Initial, PlaygroundApp.render, new MyHandler[IO]) }
//      .as(ExitCode.Success)
}

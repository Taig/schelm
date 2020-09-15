package io.taig.schelm.playground

import cats.effect.{ExitCode, IO, IOApp}
import io.taig.schelm.css.data.{CssHtml, JsCssSchelm}
import io.taig.schelm.dsl.data.DslWidget
import io.taig.schelm.interpreter.BrowserDom
import org.scalajs.dom.document

object JsPlaygroundApp extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val dom = BrowserDom[IO]

    JsCssSchelm
      .default[IO, Event](dom)(document.getElementById("main"))
      .flatMap(
        _.start(
          PlaygroundApp.Initial,
          (_: State) => DslWidget.toCssHtml(PlaygroundApp.render("hello").widget, Theme(background = "red")),
          new MyHandler[IO]
        )
      )
      .as(ExitCode.Success)
  }
}

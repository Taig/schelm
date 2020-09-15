package io.taig.schelm.playground

import cats.effect.{ExitCode, IO, IOApp}
import io.taig.schelm.css.data.JsCssSchelm
import io.taig.schelm.dsl.data.DslWidget
import io.taig.schelm.interpreter.BrowserDom
import io.taig.schelm.mdc.{MdcEvent, MdcHandler}
import org.scalajs.dom.document

object JsPlaygroundApp extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val dom = BrowserDom[IO]

    JsCssSchelm
      .default[IO, MdcEvent[Event]](dom)(document.getElementById("main"))
      .flatMap(
        _.start(
          PlaygroundApp.Initial,
          (_: State) =>
            DslWidget.toCssHtml(DslWidget.toWidget(PlaygroundApp.render("hello")), Theme(background = "red")),
          MdcHandler[IO, State, Event, Nothing](new MyHandler[IO])
        )
      )
      .as(ExitCode.Success)
  }
}

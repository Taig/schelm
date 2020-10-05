package io.taig.schelm.playground

import cats.effect.{ExitCode, IO, IOApp}
import io.taig.schelm.css.data.JsCssSchelm
import io.taig.schelm.dsl.JsDslWidgetSchelm
import io.taig.schelm.dsl.data.DslWidget
import io.taig.schelm.interpreter.BrowserDom
import io.taig.schelm.mdc.MdcTheme
import org.scalajs.dom.document

object JsPlaygroundApp extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val dom = BrowserDom[IO]

    JsDslWidgetSchelm[IO, MdcTheme]
      .start(PlaygroundApp.render[IO]("yolo"))
      .as(ExitCode.Success)
  }
}

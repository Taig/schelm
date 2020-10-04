package io.taig.schelm.playground

import cats.effect.{ExitCode, IO, IOApp}
import io.taig.schelm.css.data.JsCssSchelm
import io.taig.schelm.dsl.data.DslWidget
import io.taig.schelm.interpreter.BrowserDom
import org.scalajs.dom.document

object JsPlaygroundApp extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val dom = BrowserDom[IO]

    JsCssSchelm
      .default[IO](dom)(document.getElementById("main"))
      .flatMap(
        _.start(
          ???,
          ???
        )
      )
      .as(ExitCode.Success)
  }
}

package io.taig.schelm

import cats.effect.{ExitCode, IO, IOApp}
import io.taig.schelm.css._
import org.scalajs.dom

object Playground extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val globals = Stylesheet.of(normalize)

    for {
      schelm <- HtmlBrowserSchelm[IO, Event, dom.Node]
      //render <- Css.enable(globals, dom, App.widget)
      _ <- schelm.start("main", State(), ???, App.events, App.commands)
    } yield ExitCode.Success
  }
}

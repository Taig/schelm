package io.taig.schelm

import cats.effect.{ExitCode, IO, IOApp}
import io.taig.schelm.css._
import org.scalajs.dom

object Playground extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      main <- IO(dom.document.getElementById("main"))
      globals = Stylesheet.of(normalize)
      render <- Css.enable[IO, State, Event](globals, App.widget)
      _ <- Schelm.start(main)(State(), render, App.events, App.commands)
    } yield ExitCode.Success
}

package com.ayendo.schelm

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import com.ayendo.schelm.css._
import com.ayendo.schelm.dsl.Dsl
import org.scalajs.dom

object Playground extends IOApp with Dsl[Event] {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      main <- IO(dom.document.getElementById("main"))
      global = Stylesheet.of(normalize)
      registry <- CssRegistry[IO](global)
      render = Css.enable(registry, App.widget)
      _ <- Schelm.start(main)(State(), render, App.events, App.commands)
    } yield ExitCode.Success
}

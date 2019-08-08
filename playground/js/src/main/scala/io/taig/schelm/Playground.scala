package io.taig.schelm

import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream
import io.taig.schelm.css._
import io.taig.schelm.playground.{App, Event, State}

object Playground extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      manager <- EventManager.unbounded[IO, Event]
      dom <- BrowserDom[IO, Event](manager)
      schelm = CssSchelm(manager, dom, normalize)
      _ <- schelm.start(
        "main",
        State(),
        (state: State) => toStyledHtml(App.widget(state)),
        App.events,
        App.commands,
        Stream.empty
      )
    } yield ExitCode.Success
}

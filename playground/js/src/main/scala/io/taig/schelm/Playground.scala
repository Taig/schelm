package io.taig.schelm

import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream
import io.taig.schelm.css._

object Playground extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val globals = Stylesheet.of(normalize)

    for {
      schelm <- WidgetBrowserSchelm[IO, Event]
      _ <- schelm.start(
        "main",
        State(),
        (state: State) =>
          (StyledHtml.apply[Event] _ tupled)(App.widget(state).render),
        App.events,
        App.commands,
        Stream.empty
      )
    } yield ExitCode.Success
  }
}

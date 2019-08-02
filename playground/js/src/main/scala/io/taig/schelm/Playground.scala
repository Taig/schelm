package io.taig.schelm

import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream
import io.taig.schelm.css._
import org.scalajs.dom

object Playground extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val globals = Stylesheet.of(normalize)

    for {
      manager <- EventManager.unbounded[IO, Event]
      xxx <- BrowserDom[IO, Event](manager)
      container <- xxx.getElementById("main").map(_.get)
      schelm = Schelm(
        WidgetRenderer[IO, Event, dom.Node](xxx),
        StyledReferenceAttacher[IO, Event, dom.Node](xxx),
        StyledHtmlDiffer[Event],
        ???
      )
      _ <- schelm.start[State, Command](
        container,
        State(),
        App.widget,
        App.events,
        Stream.empty
      )
    } yield ExitCode.Success
  }
}

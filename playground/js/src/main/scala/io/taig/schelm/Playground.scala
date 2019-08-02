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
      renderer = HtmlRenderer[IO, Event, dom.Node](xxx)
      schelm = Schelm[IO, Event, dom.Node, StyledHtml[Event], StyledReference[
        Event,
        dom.Node
      ], StyledDiff[Event]](
        manager,
        StyledHtmlRenderer[IO, Event, dom.Node](renderer),
        StyledReferenceAttacher[IO, Event, dom.Node](xxx),
        StyledHtmlDiffer[Event],
        StyledReferencePatcher[IO, Event, dom.Node](renderer, xxx)
      )
      _ <- schelm.start[State, Command](
        container,
        State(),
        state => (StyledHtml.apply[Event] _ tupled)(App.widget(state).render),
        App.events,
        App.commands,
        Stream.empty
      )
    } yield ExitCode.Success
  }
}

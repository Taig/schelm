package io.taig.schelm

import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream
import io.taig.schelm.css._
import io.taig.schelm.playground.{App, Event, State}

object Playground extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    for {
      manager <- EventManager.unbounded[IO, Event]
      dom <- BrowserDom[IO, Event](manager)
      renderer = HtmlRenderer(dom)
      attacher = StyledReferenceAttacher(dom)
      patcher <- StyledReferencePatcher(dom, renderer)
      schelm = Schelm(
        dom,
        manager,
        StyledHtmlRenderer(renderer),
        attacher,
        StyledHtmlDiffer[Event],
        patcher
      )
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
}

package io.taig.schelm

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import fs2.Stream
import io.taig.schelm.css._

object Playground extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    for {
      manager <- EventManager.unbounded[IO, Event]
      dom <- BrowserDom[IO, Event](manager)
      renderer = HtmlRenderer(dom)
      attacher = StyledReferenceAttacher(dom)
      schelm = Schelm(
        dom,
        manager,
        StyledHtmlRenderer(renderer),
        attacher,
        StyledHtmlDiffer[Event],
        StyledReferencePatcher(dom, renderer)
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

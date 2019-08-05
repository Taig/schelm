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
      html = toStyledHtml(App.widget(State()))
      nodes <- renderer.render(App.html, Path.Empty)
      _ <- nodes.traverse_(node => IO(org.scalajs.dom.console.dir(node)))
    } yield ExitCode.Success
  }
}

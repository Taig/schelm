package io.taig.schelm.playground

import cats.effect.{ExitCode, IO, IOApp}
import io.taig.schelm.interpreter.{BrowserDom, HtmlRenderer, QueueEventManager}

object Playground extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    QueueEventManager
      .unbounded[IO, Shared.Event]
      .flatMap { events =>
        val dom = BrowserDom(events)
        val renderer = HtmlRenderer(dom)

        renderer.render(Shared.component.html).map { nodes =>
          nodes.foreach { node => org.scalajs.dom.console.dir(node) }
        }
      }
      .as(ExitCode.Success)
  }
}

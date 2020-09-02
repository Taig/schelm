package io.taig.schelm.playground

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import io.taig.schelm.interpreter.{BrowserDom, HtmlRenderer, QueueEventManager}

object Playground extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    QueueEventManager
      .unbounded[IO, Shared.Event]
      .flatMap { events =>
        val dom = BrowserDom(events)
        val renderer = HtmlRenderer(dom)

        renderer.render(Shared.component.html).flatMap { nodes =>
          dom
            .getElementById("main")
            .flatMap(_.liftTo[IO](new IllegalStateException))
            .flatMap { root => nodes.traverse_(dom.appendChild(root, _)) }
        }
      }
      .as(ExitCode.Success)
  }
}

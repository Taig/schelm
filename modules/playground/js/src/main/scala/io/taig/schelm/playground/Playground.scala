package io.taig.schelm.playground

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import io.taig.schelm.interpreter.{BrowserDom, HtmlDiffer, HtmlPatcher, HtmlRenderer, QueueEventManager}

object Playground extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    QueueEventManager
      .unbounded[IO, Shared.Event]
      .flatMap { events =>
        val dom = BrowserDom(events)
        val renderer = HtmlRenderer(dom)
        val differ = HtmlDiffer[Shared.Event]
        val patcher = HtmlPatcher(dom, renderer)
        val previous = Shared.html("yolo")
        val next = Shared.html("foobar")

        renderer
          .render(previous)
          .flatTap { nodes =>
            dom
              .getElementById("main")
              .flatMap(_.liftTo[IO](new IllegalStateException))
              .flatMap { root => nodes.traverse_(dom.appendChild(root, _)) }
          }
          .flatMap { nodes =>
            differ.diff(previous, next) match {
              case Some(diff) =>
                println(diff)
                patcher.patch(nodes, diff)
              case None => IO.unit
            }
          }
      }
      .as(ExitCode.Success)
  }
}

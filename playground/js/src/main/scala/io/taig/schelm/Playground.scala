package io.taig.schelm

import cats.effect.{ExitCode, IO, IOApp}
import fs2.concurrent.Queue
import io.taig.schelm.css._

object Playground extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val globals = Stylesheet.of(normalize)
    for {
      queue <- Queue.unbounded[IO, Event]
      send = { action: Event =>
        queue.enqueue1(action).runAsync(_ => IO.unit).unsafeRunSync()
      }
      dom = BrowserDom[IO, Event](send)
      render <- Css.enable(globals, dom, App.widget)
      _ <- Schelm.start("main", dom, queue)(
        State(),
        render,
        App.events,
        App.commands
      )
    } yield ExitCode.Success
  }
}

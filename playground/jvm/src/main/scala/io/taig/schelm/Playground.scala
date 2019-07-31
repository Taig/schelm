package io.taig.schelm

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._

object Playground extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val renderer = ServerRenderer[IO, Event]
    val widget = App.widget(State())
    val html = widget.html
    renderer.render(html).flatMap(node => IO(println(node.head))) *>
      IO(ExitCode.Success)
  }
}

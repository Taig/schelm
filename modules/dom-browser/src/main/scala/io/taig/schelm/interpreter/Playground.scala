package io.taig.schelm.interpreter

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import io.taig.schelm.algebra.Dom
import io.taig.schelm.data._

object Playground extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val dom = BrowserDom[IO]

    val html = Html(
      Component.Text(
        "hello world",
        Listeners.Empty,
        lifecycle = Lifecycle(
          mounted = new Callback.Text[IO] {
            override def apply(dom: Dom[IO])(reference: dom.Text): IO[Unit] =
            IO(println("I'm attached to le dom <3"))
          },
          unmount = Callback.Text.noop[IO]
        )
      )
    )

    dom.getElementById("main").flatMap(_.liftTo[IO](new IllegalStateException)).flatMap { root =>
      val renderer = HtmlRenderer(dom)
      val attacher = HtmlReferenceAttacher.default(dom)(root)
      renderer.render(html).flatMap { reference => attacher.attach(reference) }
    } *> IO(ExitCode.Success)
  }
}

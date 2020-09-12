package io.taig.schelm.interpreter

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import io.taig.schelm.algebra.Dom
import io.taig.schelm.data._

object Playground extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val dom = BrowserDom

    val html = Html(
      Component.Text(
        "hello world",
        Listeners.Empty,
        lifecycle = Lifecycle(
          mounted = Some(new Callback.Text[IO] {
            override def apply(dom: Dom)(reference: dom.Text): IO[Unit] =
              IO(dom.data(reference, "yolo"))
          }),
          unmount = None
        )
      )
    )

    IO(dom.getElementById("main")).flatMap(_.liftTo[IO](new IllegalStateException)).flatMap { root =>
      val renderer = HtmlRenderer(dom)
      val attacher = HtmlReferenceAttacher.default(dom)(root)
      renderer.render(html).flatMap { reference => attacher.attach(reference) }
    } *> IO(ExitCode.Success)
  }
}

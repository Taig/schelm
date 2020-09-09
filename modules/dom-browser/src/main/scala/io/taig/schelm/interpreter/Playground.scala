package io.taig.schelm.interpreter

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import io.taig.schelm.data._

object Playground extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val dom = BrowserDom[IO]

    val html = LifecycleHtml(
      Lifecycle(
        value = Component.Text("hello world", Listeners.Empty),
        mounted = (reference: HtmlReference[dom.Node, dom.Element, dom.Text]) => IO(println(s"lol here we are: ${reference.reference}"))
//        unmount = (_: HtmlReference[Nothing, dom.Node, dom.Element, dom.Text]) => IO.unit
      )
    )

    dom.getElementById("main").flatMap(_.liftTo[IO](new IllegalStateException)).flatMap { root =>
      val renderer = LifecycleHtmlRenderer(dom)
      val attacher = LifecycleHtmlReferenceAttacher.default(dom)(root)
      renderer.render(html).flatMap { reference => attacher.attach(reference) }
    } *> IO(ExitCode.Success)
  }
}

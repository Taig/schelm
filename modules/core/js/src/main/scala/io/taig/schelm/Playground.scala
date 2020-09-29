package io.taig.schelm

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Patcher}
import io.taig.schelm.data._
import io.taig.schelm.interpreter.{BrowserDom, HtmlReferenceAttacher, HtmlRenderer, QueueStateManager}

object Playground extends IOApp {
  val html: Html[IO] = Html(
    Node.Stateful[IO, String, Html[IO]](
      initial = "foobar",
      render = (update, state) =>
        Html(
          Node.Element(
            Tag(
              "button",
              Attributes.Empty,
              Listeners.of(Listener(Listener.Name("click"), event => update("yolo")))
            ),
            Node.Element.Variant.Normal(
              Children.of(
                Html(Node.Text(state, listeners = Listeners.Empty, lifecycle = Lifecycle.Text.Noop))
              )
            ),
            Lifecycle.Element.Noop
          )
        )
    )
  )

  override def run(args: List[String]): IO[ExitCode] = {
    val dom = BrowserDom[IO]

    dom
      .getElementById("main")
      .flatMap(_.liftTo[IO](new IllegalStateException))
      .map(HtmlReferenceAttacher.default[IO](dom)(_))
      .flatMap { attacher =>
        QueueStateManager.empty[IO].flatMap { manager =>
          val renderer = HtmlRenderer(dom, Patcher.noop[IO, List[Dom.Node], HtmlDiff[IO]], manager)
          manager.subscription.map(println).compile.drain.start *>
            renderer.render(html, Path.of(Key.Index(0))).flatTap(attacher.attach)
        }
      } *> IO(ExitCode.Success)
  }
}

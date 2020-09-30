package io.taig.schelm

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Patcher, StateManager}
import io.taig.schelm.data._
import io.taig.schelm.interpreter.{
  BrowserDom,
  HtmlDiffer,
  HtmlPatcher,
  HtmlReferenceAttacher,
  HtmlRenderer,
  QueueStateManager
}

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

    val differ = HtmlDiffer[IO]

    dom
      .getElementById("main")
      .flatMap(_.liftTo[IO](new IllegalStateException))
      .map(HtmlReferenceAttacher.default[IO](dom)(_))
      .flatMap { attacher =>
        QueueStateManager.empty[IO].flatMap { manager =>
          val renderer = HtmlRenderer(dom, manager)
          manager.subscription
            .evalMap {
              case StateManager.Event(node, reference, state, update) =>
                val newHtml = node.render(update, state)
                println(differ.diff(reference.html, newHtml))

                IO("")
            }
            .evalTap(event => IO(println(event)))
            .compile
            .drain
            .start *>
            renderer.render(html).flatTap(attacher.attach)
        }
      } *> IO(ExitCode.Success)
  }
}

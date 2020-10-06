package io.taig.schelm

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import io.taig.schelm.algebra.Dom
import io.taig.schelm.data._
import io.taig.schelm.interpreter._

import scala.concurrent.duration.DurationInt

object Playground extends IOApp {
  val app: StateHtml[IO] = StateHtml(
    State.Stateful[IO, Int, Node[IO, Listeners[IO], StateHtml[IO]]](
      initial = 1,
      render = { (update, state) =>
        Node.Element(
          Tag(
            "button",
            Attributes.Empty,
            Listeners.of(Listener(Listener.Name("click"), _ => update(_ - 1)))
          ),
          Node.Element.Variant.Normal(
            Children.of(
              StateHtml(
                State.Stateless(Node.Text(state.toString, listeners = Listeners.Empty, lifecycle = Lifecycle.Noop))
              )
            )
          ),
          Lifecycle {
            Some { (_: Dom.Element) =>
              fs2.Stream
                .awakeEvery[IO](1.second)
                .evalMap(_ => update(state => state + 1))
                .compile
                .drain
                .background
                .void
            }
          }
        )
      }
    )
  )

  override def run(args: List[String]): IO[ExitCode] = {
    val dom = BrowserDom[IO]

    dom
      .getElementById("main")
      .flatMap(_.liftTo[IO](new IllegalStateException("root element does not exist")))
      .flatMap(StateHtmlSchelm.empty[IO](dom)(_))
      .flatMap(_.start(app).use(_ => IO.never))
      .as(ExitCode.Success)
  }
}

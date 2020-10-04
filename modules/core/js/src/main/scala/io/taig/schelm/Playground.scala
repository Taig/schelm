package io.taig.schelm

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import io.taig.schelm.algebra.Dom
import io.taig.schelm.data._
import io.taig.schelm.interpreter._

import scala.concurrent.duration.DurationInt

object Playground extends IOApp {
  val html: ComponentHtml[IO] = ComponentHtml(
    State.Stateful[IO, Int, Node[IO, Listeners[IO], ComponentHtml[IO]]](
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
              ComponentHtml(
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

    val differ = HtmlDiffer[IO]

    for {
      root <- dom.getElementById("main").flatMap(_.liftTo[IO](new IllegalStateException("root element does not exist")))
      states <- QueueStateManager.empty[IO]
      statesRenderer = ComponentRenderer(states)
      htmlRenderer = HtmlRenderer[IO](dom)
      patcher = HtmlPatcher[IO](dom, htmlRenderer)
      attacher = HtmlReferenceAttacher.default(dom)(root)
      html <- statesRenderer.render(html).run(Path.Empty)
      reference <- htmlRenderer.render(html)
      _ <- states.subscription
        .evalScan(reference) { (reference, update) =>
          reference.update(update.path) { reference =>
            differ
              .diff(HtmlReference(reference).html, update.html)
              .traverse(patcher.patch(HtmlReference(reference), _))
              .map(_.map(_.reference).getOrElse(reference))
          }
        }
        .handleErrorWith { throwable => fs2.Stream.eval(IO(throwable.printStackTrace())) }
        .compile
        .drain
        .start
      _ <- attacher.attach(reference)
    } yield ExitCode.Success
  }
}

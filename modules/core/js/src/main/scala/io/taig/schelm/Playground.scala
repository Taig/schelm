package io.taig.schelm

import cats.effect.{ExitCode, IO, IOApp, Resource}
import cats.implicits._
import io.taig.schelm.data._
import io.taig.schelm.interpreter._

import scala.concurrent.duration.DurationInt

object Playground extends IOApp {
  val html: Html[IO] = Html(
    Node.Stateful[IO, Int, Html[IO]](
      0,
      render = { (update, state) =>
        Html(
          Node.Element(
            Tag(
              "button",
              Attributes.Empty,
              Listeners.of(Listener(Listener.Name("click"), event => IO(println("le click"))))
            ),
            Node.Element.Variant.Normal(
              Children.of(Html(Node.Text(state.toString, listeners = Listeners.Empty, lifecycle = Lifecycle.noop[IO])))
            ),
            _ =>
              fs2.Stream
                .awakeEvery[IO](1.second)
                .evalMap(_ => update(state => state + 1))
                .compile
                .drain
                .background
                .void
          )
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
      renderer = HtmlRenderer[IO](dom, states)
      patcher = HtmlPatcher[IO](dom, renderer)
      attacher = HtmlReferenceAttacher.default(dom)(root)
      reference <- renderer.render(html, Path.Empty)
      _ <- states.subscription
        .evalScan(reference) { (reference, update) =>
          reference.update(update.path) {
            case reference @ NodeReference.Stateful(html, value) =>
              val next = html.render(value => states.submit(update.path, update.initial, value), update.state)
              val diff = differ.diff(value.html, next)
              diff
                .traverse(patcher.patch(HtmlReference(reference), _))
                .map(_.map(_.reference).getOrElse(reference))
            case reference => IO(reference)
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

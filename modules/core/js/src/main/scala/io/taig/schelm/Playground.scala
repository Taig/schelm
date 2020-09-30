package io.taig.schelm

import cats.effect.concurrent.Ref
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import io.taig.schelm.algebra.StateManager
import io.taig.schelm.data._
import io.taig.schelm.interpreter._

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
              Listeners.of(Listener(Listener.Name("click"), event => update(state.reverse)))
            ),
            Node.Element.Variant.Normal(
              Children.of(
                Html(Node.Text(state, listeners = Listeners.Empty, lifecycle = Lifecycle.Noop))
              )
            ),
            Lifecycle.Noop
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
          HtmlRenderer.default(dom, manager).flatMap { renderer =>
            val patcher = HtmlPatcher(dom, renderer)

            manager.subscription
              .evalMap {
                case event @ StateManager.Event(reference, previous, next) =>
                  Ref[IO].of(none[NodeReference.Stateful[IO, HtmlReference[IO]]]).flatMap { result =>
                    val update = (value: Any) => result.get.flatMap(_.liftTo[IO](new IllegalStateException)).flatMap(manager.submit(_, value))
                    val prevHtml = reference.reference.html
                    val nextHtml = ??? // reference.render(update, next)

                    val diff = differ.diff(prevHtml, nextHtml)
                    println(diff)
                    diff.traverse_(patcher.patch(reference.reference, _))
                  }
              }
              .handleErrorWith { throwable =>
                fs2.Stream.eval {
                  IO {
                    System.err.println("Failed to patch DOM")
                    throwable.printStackTrace(System.err)
                  }
                }
              }
              .compile
              .drain
              .start *>
              renderer.render(html).flatTap(attacher.attach)
          }
        }
      } *> IO(ExitCode.Success)
  }
}

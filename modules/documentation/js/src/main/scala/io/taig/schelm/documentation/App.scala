package io.taig.schelm.documentation

import cats.effect.std.Dispatcher
import cats.effect.{IO, IOApp}
import io.taig.schelm.css.data.Css
import io.taig.schelm.data.{Contextual, Listener, Listeners, Namespace, Node, State}
import io.taig.schelm.dsl._
import io.taig.schelm.interpreter.BrowserDom
import io.taig.schelm.redux.data.Redux
import io.taig.schelm.ui.{Theme, Typography}
import org.scalajs.dom.{document, window}

object App extends IOApp.Simple {
  val app = div
    .children(
      Typography.h1("hello world (:"),
      Typography.h2("hello world (:"),
      Typography.body1("hello world (:")
    )

  override def run: IO[Unit] = Dispatcher[IO]
    .map(BrowserDom[IO])
    .flatMap { dom =>
      Schelm.start(dom)(document.getElementById("main"), Theme.Default, app)
    }
    .use(_ => IO.unit)
}

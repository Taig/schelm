package io.taig.schelm.documentation

import cats.effect.{ExitCode, IO, IOApp, Resource}
import cats.effect.std.Dispatcher
import cats.implicits._
import io.taig.schelm.dsl._
import io.taig.schelm.interpreter.BrowserDom
import io.taig.schelm.material.MaterialTheme

object Main extends IOApp {
  def update(state: State, event: Event): State = event match {
    case Event.TextChanged(value) => state.copy(text = value.take(20))
  }

  override def run(args: List[String]): IO[ExitCode] =
    (for {
      dispatcher <- Dispatcher[IO]
      dom = BrowserDom[IO](dispatcher)
      root <- Resource.liftF(dom.getElementById("main").flatMap(_.liftTo[IO](new IllegalStateException)))
      schelm <- Resource.liftF(Schelm.default[IO, Event, MaterialTheme](dom)(root))
      _ <- schelm.start(State.Initial)(_ => MaterialTheme.Default, App[IO], update)
    } yield ExitCode.Success).use(_ => IO.never)
}

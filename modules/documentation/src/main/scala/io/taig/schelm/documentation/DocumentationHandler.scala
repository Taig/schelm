package io.taig.schelm.documentation

import cats.Applicative
import io.taig.schelm.algebra.Handler
import io.taig.schelm.data.Result
import cats.implicits._

final class DocumentationHandler[F[_]: Applicative] extends Handler[F, State, Event, Command] {
  override val command: Command => F[Option[Event]] = _ => none[Event].pure[F]

  override val event: (State, Event) => Result[State, Command] = (_, _) => Result.Empty
}

object DocumentationHandler {
  def apply[F[_]: Applicative]: Handler[F, State, Event, Command] = new DocumentationHandler[F]
}

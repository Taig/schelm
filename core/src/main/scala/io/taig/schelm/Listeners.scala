package io.taig.schelm

import cats.Applicative
import cats.implicits._

final case class Listeners[Event](values: Map[String, Action[Event]])
    extends AnyVal {
  def toList: List[Listener[Event]] = values.toList.map {
    case (event, action) => Listener(event, action)
  }

  def traverse_[F[_]: Applicative, B](f: Listener[Event] => F[B]): F[Unit] =
    toList.traverse_(f)
}

object Listeners {
  def empty[Event]: Listeners[Event] = Listeners(Map.empty)
}

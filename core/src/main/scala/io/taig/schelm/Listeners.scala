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

  def of[A](values: Listener[A]*): Listeners[A] = from(values)

  def from[A](values: Iterable[Listener[A]]): Listeners[A] =
    Listeners(
      Map(values.map(listener => listener.event -> listener.action).toSeq: _*)
    )
}

package io.taig.schelm.redux.data

import cats.Functor
import io.taig.schelm.redux.algebra.EventManager

sealed abstract class Redux[+F[_], +Event, +A] extends Product with Serializable

object Redux {
  final case class Pure[A](value: A) extends Redux[Nothing, Nothing, A]
  final case class Render[F[_], Event, A](f: EventManager[F, Event] => A) extends Redux[F, Event, A]

  def run[F[_], Event, A](events: EventManager[F, Event]): Redux[F, Event, A] => A = {
    case Pure(value)                => value
    case redux: Render[F, Event, A] => redux.f(events)
  }

  implicit def functor[F[_], Event]: Functor[Redux[F, Event, *]] = new Functor[Redux[F, Event, *]] {
    override def map[A, B](fa: Redux[F, Event, A])(f: A => B): Redux[F, Event, B] = fa match {
      case Pure(value)                => Pure(f(value))
      case redux: Render[F, Event, A] => Render[F, Event, B](manager => f(redux.f(manager)))
    }
  }
}

package io.taig.schelm.redux.data

import io.taig.schelm.redux.algebra.EventManager
import io.taig.schelm.redux.instances.ReduxInstances

sealed abstract class Redux[+F[_], +A, +B] extends Product with Serializable

object Redux extends ReduxInstances {
  final case class Pure[A](value: A) extends Redux[Nothing, Nothing, A]
  final case class Render[F[_], A, B](f: EventManager[F, A] => B) extends Redux[F, A, B]

  implicit class ReduxOps[F[_], A, B](val redux: Redux[F, A, B]) extends AnyVal {
    def provide(events: EventManager[F, A]): B = redux match {
      case Pure(value) => value
      case Render(f)   => f(events)
    }
  }

  def apply[F[_], A, B](f: EventManager[F, A] => B): Redux[F, A, B] = Render(f)

  def pure[A](value: A): Redux[Nothing, Nothing, A] = Pure(value)
}

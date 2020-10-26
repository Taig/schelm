package io.taig.schelm.data

import cats.Functor

sealed abstract class State[+F[_], +A] extends Product with Serializable

final case class Stateful[F[_], A, B](initial: A, render: ((A => A) => F[Unit], A) => State[F, B]) extends State[F, B] {
  def map[C](f: B => C): Stateful[F, A, C] = ??? // copy(render = (update, current) => f(render(update, current)))
}

final case class Stateless[A](value: A) extends State[Nothing, A]

object State {
  def run[F[_], A, B](current: A): State[F, B] => B = {
    case state: Stateful[F, _, B] => ??? // state.render(???, current)
    case Stateless(value)         => value
  }

  implicit def functor[F[_]]: Functor[State[F, *]] = new Functor[State[F, *]] {
    override def map[A, B](fa: State[F, A])(f: A => B): State[F, B] =
      fa match {
        case state: Stateful[F, _, A] => state.map(f)
        case state: Stateless[A]      => state.copy(value = f(state.value))
      }
  }
}

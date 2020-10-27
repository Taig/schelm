package io.taig.schelm.data

import cats.Functor

sealed abstract class State[+F[_], +A] extends Product with Serializable {
  def map[B](f: A => B): State[F, B]
}

final case class Stateful[F[_], A, B](initial: A, render: ((A => A) => F[Unit], A) => State[F, B]) extends State[F, B] {
  override def map[C](f: B => C): State[F, C] =
    copy[F, A, C](render = (update, current) => render(update, current).map(f))
}

final case class Stateless[A](value: A) extends State[Nothing, A] {
  override def map[B](f: A => B): State[Nothing, B] = copy(value = f(value))
}

object State {
  implicit def functor[F[_]]: Functor[State[F, *]] = new Functor[State[F, *]] {
    override def map[A, B](fa: State[F, A])(f: A => B): State[F, B] = fa.map(f)
  }
}

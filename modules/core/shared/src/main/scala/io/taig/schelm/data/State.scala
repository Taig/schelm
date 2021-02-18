package io.taig.schelm.data

import cats.syntax.all._
import io.taig.schelm.algebra.StateManager
import io.taig.schelm.instance.StateInstances

sealed abstract class State[+F[_], +A] extends Product with Serializable {
  def map[B](f: A => B): State[F, B]
}

object State extends StateInstances {
  final case class Stateful[F[_], A, B](
      identifier: Identifier,
      default: A,
      render: ((A => A) => F[Unit], A) => State[F, B]
  ) extends State[F, B] {
    override def map[C](f: B => C): State[F, C] =
      copy[F, A, C](render = (update, current) => render(update, current).map(f))

    private[schelm] def internalStateCurrent(states: Tree[Any]): Either[RuntimeException, A] =
      Either.catchOnly[ClassCastException](states.payload.map(_.asInstanceOf[A]).getOrElse(default))

    private[schelm] def internalStateUpdate(
        manager: StateManager[F],
        identification: Identification
    ): (A => A) => F[Unit] =
      apply => manager.submit(StateManager.Update(identification, default, apply))
  }

  final case class Stateless[A](value: A) extends State[Nothing, A] {
    override def map[B](f: A => B): State[Nothing, B] = copy(value = f(value))
  }

  @inline
  def stateful[F[_], A, B](
      identifier: Identifier,
      default: A,
      render: ((A => A) => F[Unit], A) => State[F, B]
  ): State[F, B] = Stateful(identifier, default, render)

  @inline
  def stateless[A](value: A): State[Nothing, A] = Stateless(value)
}

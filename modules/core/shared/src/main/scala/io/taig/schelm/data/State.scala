package io.taig.schelm.data

import cats._
import io.taig.schelm.algebra.StateManager
import io.taig.schelm.util.NodeFunctor
import io.taig.schelm.util.NodeFunctor.ops._

sealed abstract class State[+F[_], +A] extends Product with Serializable {
  def map[B](f: A => B): State[F, B]
}

final case class Stateful[F[_], A, B](
    identifier: Identifier,
    default: A,
    render: ((A => A) => F[Unit], A) => State[F, B]
) extends State[F, B] {
  override def map[C](f: B => C): State[F, C] =
    copy[F, A, C](render = (update, current) => render(update, current).map(f))

  private[schelm] def internalMapAttributes(f: Attributes => Attributes)(implicit node: NodeFunctor[B]): State[F, B] =
    Stateful[F, A, B](identifier, default, (update, current) => render(update, current).mapAttributes(f))

  private[schelm] def internalStateUpdate(
      manager: StateManager[F],
      identification: Identification
  ): (A => A) => F[Unit] = apply => manager.submit(StateManager.Update(identification, identifier, default, apply))

  private[schelm] def internalStateCurrent(states: Map[Identifier, Any]): A =
    try states.getOrElse(identifier, default).asInstanceOf[A]
    catch { case _: ClassCastException => default }
}

final case class Stateless[A](value: A) extends State[Nothing, A] {
  override def map[B](f: A => B): State[Nothing, B] = copy(value = f(value))
}

object State {
  implicit def functor[F[_]]: Functor[State[F, *]] = new Functor[State[F, *]] {
    override def map[A, B](fa: State[F, A])(f: A => B): State[F, B] = fa.map(f)
  }

  implicit def node[F[_], A: NodeFunctor]: NodeFunctor[State[F, A]] = new NodeFunctor[State[F, A]] {
    override def mapAttributes(state: State[F, A])(f: Attributes => Attributes): State[F, A] =
      state match {
        case state: Stateful[F, _, A] => state.internalMapAttributes(f)
        case Stateless(value)         => Stateless(value.mapAttributes(f))
      }
  }
}

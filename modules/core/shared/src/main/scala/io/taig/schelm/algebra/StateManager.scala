package io.taig.schelm.algebra

import cats.effect.kernel.Spawn
import cats.syntax.all._
import fs2.Stream
import io.taig.schelm.data.{Identification, Tree}

abstract class StateManager[F[_]] {
  def submit[A](update: StateManager.Update[A]): F[Unit]

  def get[A](identification: Identification): F[Option[A]]

  def subscription: Stream[F, StateManager.Update[_]]

  def snapshot: F[Tree[Any]]
}

object StateManager {
  final case class Update[A](identification: Identification, default: A, apply: A => A)

  def noop[F[_]](implicit F: Spawn[F]): StateManager[F] = new StateManager[F] {
    override def submit[A](update: Update[A]): F[Unit] = F.unit

    override def get[A](identification: Identification): F[Option[A]] = none[A].pure[F]

    override val subscription: Stream[F, Update[_]] = Stream.eval(F.never)

    override val snapshot: F[Tree[Any]] = Tree.Empty.pure[F].widen
  }
}

package io.taig.schelm.data

import cats.Monoid

final case class ListenerTree[+F[_], +A](listeners: Listeners[F], children: A)

object ListenerTree {
  def empty[F[_], A](implicit monoid: Monoid[A]): ListenerTree[F, A] = ListenerTree(Listeners.Empty, monoid.empty)

  val Empty: ListenerTree[Nothing, Children.Identifiers[Nothing]] =
    ListenerTree(Listeners.Empty, Children.Identifiers(Children.Indices(Map.empty), Map.empty))

  sealed abstract class Children[+F[_]] extends Product with Serializable

  object Children {
    final case class Indices[+F[_]](values: Map[Int, ListenerTree[F, Indices[F]]]) extends Children[F]
    final case class Identifiers[+F[_]](indices: Indices[F], identifiers: Map[Identifier, ListenerTree[F, Children[F]]])
        extends Children[F]
  }
}

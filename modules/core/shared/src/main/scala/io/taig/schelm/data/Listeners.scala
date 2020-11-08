package io.taig.schelm.data

import cats.Monoid

final case class Listeners[+F[_]](values: Map[Listener.Name, Listener.Action[F]]) extends AnyVal {
  def isEmpty: Boolean = values.isEmpty

  def ++[G[a] >: F[a]](listeners: Listeners[G]): Listeners[G] = Listeners(values ++ listeners.values)

  def +[G[a] >: F[a]](listener: Listener[G]): Listeners[G] = Listeners(values + listener.toTuple)

  @inline
  def get(name: Listener.Name): Option[Listener.Action[F]] = values.get(name)

  @inline
  def keys: Set[Listener.Name] = values.keySet

  def toList: List[Listener[F]] = values.map { case (name, action) => Listener(name, action) }.toList
}

object Listeners {
  def empty[F[_]]: Listeners[F] = Listeners(Map.empty)

  val Empty: Listeners[Nothing] = empty

  def from[F[_]](listeners: Iterable[Listener[F]]): Listeners[F] = Listeners(listeners.map(_.toTuple).toMap)

  def of[F[_]](listeners: Listener[F]*): Listeners[F] = from(listeners)

  implicit def monoid[F[_]]: Monoid[Listeners[F]] = new Monoid[Listeners[F]] {
    override val empty: Listeners[F] = Empty

    override def combine(x: Listeners[F], y: Listeners[F]): Listeners[F] = x ++ y
  }
}

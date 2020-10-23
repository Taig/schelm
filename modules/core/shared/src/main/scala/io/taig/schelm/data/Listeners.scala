package io.taig.schelm.data

import org.scalajs.dom.raw.{Event, EventTarget}

final case class Listeners[+F[_]](values: Map[Listener.Name, Listener.Action[F, Event, EventTarget]]) extends AnyVal {
  def isEmpty: Boolean = values.isEmpty

  def ++[G[A] >: F[A]](listeners: Listeners[G]): Listeners[G] = Listeners(values ++ listeners.values)

  def +[G[A] >: F[A]](listener: Listener[G, _ <: Event, _ <: EventTarget]): Listeners[G] =
    Listeners(values + listener.widen.toTuple)

  def toList: List[Listener[F, Event, EventTarget]] =
    values.map { case (name, action) => Listener(name, action) }.toList
}

object Listeners {
  def empty[F[_]]: Listeners[F] = Listeners(Map.empty)

  val Empty: Listeners[Nothing] = empty

  def from[F[_]](listeners: Iterable[Listener[F, Event, EventTarget]]): Listeners[F] =
    Listeners(listeners.map(_.toTuple).toMap)

  def of[F[_]](listeners: Listener[F, Event, EventTarget]*): Listeners[F] = from(listeners)
}

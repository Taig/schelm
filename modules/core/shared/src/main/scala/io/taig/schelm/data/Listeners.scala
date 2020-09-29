package io.taig.schelm.data

final case class Listeners[+F[_]](values: Map[Listener.Name, Listener.Action[F]]) extends AnyVal {
  def isEmpty: Boolean = values.isEmpty

  def ++[G[α] >: F[α]](listeners: Listeners[G]): Listeners[G] = Listeners(values ++ listeners.values)

  def +[G[α] >: F[α]](listener: Listener[G]): Listeners[G] = Listeners(values + listener.toTuple)

  def toList: List[Listener[F]] = values.map { case (name, action) => Listener(name, action) }.toList
}

object Listeners {
  def empty[F[_]]: Listeners[F] = Listeners(Map.empty)

  val Empty: Listeners[Nothing] = empty

  def from[F[_]](listeners: Iterable[Listener[F]]): Listeners[F] = Listeners(listeners.map(_.toTuple).toMap)

  def of[F[_]](listeners: Listener[F]*): Listeners[F] = from(listeners)
}

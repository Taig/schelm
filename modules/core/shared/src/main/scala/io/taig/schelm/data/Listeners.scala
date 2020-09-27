package io.taig.schelm.data

final case class Listeners[+Event](values: Map[Listener.Name, Listener.Action[Event]]) extends AnyVal {
  def isEmpty: Boolean = values.isEmpty

  def ++[A >: Event](listeners: Listeners[A]): Listeners[A] = Listeners(values ++ listeners.values)

  def +[A >: Event](listener: Listener[A]): Listeners[A] = Listeners(values + listener.toTuple)

  def toList: List[Listener[Event]] = values.map { case (name, action) => Listener(name, action) }.toList
}

object Listeners {
  def empty[Event]: Listeners[Event] = Listeners(Map.empty)

  val Empty: Listeners[Nothing] = empty[Nothing]

  def from[Event](listeners: Iterable[Listener[Event]]): Listeners[Event] = Listeners(listeners.map(_.toTuple).toMap)

  def of[Event](listeners: Listener[Event]*): Listeners[Event] = from(listeners)
}

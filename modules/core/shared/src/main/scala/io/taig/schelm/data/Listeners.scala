package io.taig.schelm.data

final case class Listeners(values: Map[Listener.Name, Listener.Action]) extends AnyVal {
  def isEmpty: Boolean = values.isEmpty

  def ++(listeners: Listeners): Listeners = Listeners(values ++ listeners.values)

  def +(listener: Listener): Listeners = Listeners(values + listener.toTuple)

  def toList: List[Listener] = values.map { case (name, action) => Listener(name, action) }.toList
}

object Listeners {
  val Empty: Listeners = Listeners(Map.empty)

  def from(listeners: Iterable[Listener]): Listeners = Listeners(listeners.map(_.toTuple).toMap)

  def of(listeners: Listener*): Listeners = from(listeners)
}

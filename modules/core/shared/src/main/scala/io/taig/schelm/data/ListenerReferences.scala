package io.taig.schelm.data

import io.taig.schelm.algebra.Dom

/** A collection of listener names and their DOM instances
  *
  * This is primarily intended as a lookup for the removal of listeners from the DOM, which requires a reference to
  * the instance.
  */
final case class ListenerReferences(values: Map[Listener.Name, Dom.Listener]) extends AnyVal {
  @inline
  def get(name: Listener.Name): Option[Dom.Listener] = values.get(name)

  def updated(name: Listener.Name, reference: Dom.Listener): ListenerReferences =
    ListenerReferences(values + (name -> reference))
}

object ListenerReferences {
  val Empty: ListenerReferences = ListenerReferences(Map.empty)

  def from(references: Iterable[(Listener.Name, Dom.Listener)]): ListenerReferences =
    ListenerReferences(references.toMap)
}

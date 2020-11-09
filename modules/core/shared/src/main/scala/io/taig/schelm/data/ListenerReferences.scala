package io.taig.schelm.data

import io.taig.schelm.algebra.Dom

final case class ListenerReferences(values: Map[Listener.Name, Dom.Listener]) extends AnyVal {
  @inline
  def get(name: Listener.Name): Option[Dom.Listener] = values.get(name)

  def updated(name: Listener.Name, reference: Dom.Listener): ListenerReferences =
    ListenerReferences(values + (name -> reference))
}

object ListenerReferences {
  def from(references: Iterable[(Listener.Name, Dom.Listener)]): ListenerReferences =
    ListenerReferences(references.toMap)
}

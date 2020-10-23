package io.taig.schelm.data

import cats.implicits._
import io.taig.schelm.algebra.Dom
import org.scalajs.dom.raw.{Event, EventTarget}

final case class ListenerReferences[+F[_]](
    values: Map[Listener.Name, (Dom.Listener[Event], Listener.Action[F, Event, EventTarget])]
) extends AnyVal {
  @inline
  def get(name: Listener.Name): Option[(Dom.Listener[Event], Listener.Action[F, Event, EventTarget])] = values.get(name)

  def updated[G[A] >: F[A]](
      name: Listener.Name,
      reference: Dom.Listener[Event],
      action: Listener.Action[G, Event, EventTarget]
  ): ListenerReferences[G] =
    ListenerReferences(values + (name -> ((reference, action))))

  def toListeners: Listeners[F] = Listeners(values.fmap(_._2))
}

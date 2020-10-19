package io.taig.schelm.data

import cats.implicits._
import io.taig.schelm.algebra.Dom

final case class ListenerReferences[+F[_]](values: Map[Listener.Name, (Dom.Listener, Listener.Action[F])])
    extends AnyVal {
  @inline
  def get(name: Listener.Name): Option[(Dom.Listener, Listener.Action[F])] = values.get(name)

  def updated[G[A] >: F[A]](
      name: Listener.Name,
      reference: Dom.Listener,
      action: Listener.Action[G]
  ): ListenerReferences[G] =
    ListenerReferences(values + (name -> ((reference, action))))

  def toListeners: Listeners[F] = Listeners(values.fmap(_._2))
}

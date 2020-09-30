package io.taig.schelm.data

import io.taig.schelm.algebra.Dom
import cats.implicits._

final case class ListenerReferences[F[_]](values: Map[Listener.Name, (Dom.Listener, Listener.Action[F])])
    extends AnyVal {
  def toListeners: Listeners[F] = Listeners(values.fmap(_._2))
}

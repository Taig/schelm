package io.taig.schelm.data

import io.taig.schelm.algebra.Dom
import cats.implicits._

final case class ListenerReferences[F[_]](values: Map[Listener.Name, (Dom.Listener, Listener.Action[F])])
    extends AnyVal {
  @inline
  def get(name: Listener.Name): Option[(Dom.Listener, Listener.Action[F])] = values.get(name)

  def updated(name: Listener.Name, reference: Dom.Listener, action: Listener.Action[F]): ListenerReferences[F] =
    ListenerReferences(values + (name -> ((reference, action))))

  def toListeners: Listeners[F] = Listeners(values.fmap(_._2))

  override def toString: String = {
    val body =
      values.map { case (name, (listener, _)) => s"${name.value}: (${listener.hashCode}, Action[F])" }.mkString(",")
    s"ListenerReferences($body)"
  }
}

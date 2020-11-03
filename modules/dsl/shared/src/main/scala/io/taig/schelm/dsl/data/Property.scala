package io.taig.schelm.dsl.data

import io.taig.schelm.css.data.Style
import io.taig.schelm.data.{Attributes, Lifecycle, Listener, Listeners}
import org.scalajs.dom.raw.{Event, EventTarget}

final case class Property[+F[_]](
    attributes: Attributes = Attributes.Empty,
    listeners: Listeners[F] = Listeners.Empty,
    style: Style = Style.Empty,
    lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
) {
  def ++[G[a] >: F[a]](property: Property[G]): Property[G] =
    Property(
      attributes ++ property.attributes,
      listeners ++ property.listeners,
      style ++ property.style,
      lifecycle ++ property.lifecycle
    )

  def modifyAttributes(f: Attributes => Attributes): Property[F] = copy(attributes = f(attributes))

  def withAttributes(attributes: Attributes): Property[F] = modifyAttributes(_ => attributes)

  def appendAttributes(attributes: Attributes): Property[F] = modifyAttributes(_ ++ attributes)

  def modifyListeners[G[a] >: F[a]](f: Listeners[G] => Listeners[G]): Property[G] =
    copy(listeners = f(listeners))

  def addListeners[G[a] >: F[a]](listeners: Listeners[G]): Property[G] =
    modifyListeners[G](_ ++ listeners)

  def addListener[G[a] >: F[a]](listener: Listener[G]): Property[G] =
    modifyListeners[G](_ + listener)

  def modifyStyle(f: Style => Style): Property[F] = copy(style = f(style))

  def withStyle(style: Style): Property[F] = modifyStyle(_ => style)

  def appendStyle(style: Style): Property[F] = modifyStyle(_ ++ style)

  def prependStyle(style: Style): Property[F] = modifyStyle(style ++ _)
}

object Property {
  val Empty: Property[Nothing] = Property()
}

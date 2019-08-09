package io.taig.schelm.dsl

import io.taig.schelm._
import io.taig.schelm.css.Styles

trait StyledHtmlDsl extends Dsl[Unit, Styles] with CssDsl[Unit, Styles] {
  override def element(name: String): Widget[Nothing, Unit, Styles] =
    Widget.pure(
      Styles.Empty,
      Component.Element(name, Attributes.Empty, Listeners.empty, Children.empty)
    )

  override def text(value: String): Widget[Nothing, Unit, Styles] =
    Widget.pure(Styles.Empty, Component.Text(value))

  override def updateStyles[A](
      widget: Widget[A, Unit, Styles],
      f: Styles => Styles
  ): Widget[A, Unit, Styles] = widget.copy(payload = f(widget.payload))
}

object StyledHtmlDsl extends StyledHtmlDsl

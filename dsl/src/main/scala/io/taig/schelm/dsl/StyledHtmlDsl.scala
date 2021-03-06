package io.taig.schelm.dsl

import cats.implicits._
import io.taig.schelm._
import io.taig.schelm.css.Styles

trait StyledHtmlDsl extends Dsl[Unit, Styles] with CssDsl[Unit] {
  override def element(name: String): Widget[Nothing, Unit, Styles] = {
    val component = Component.Element(
      name,
      namespace = None,
      Attributes.Empty,
      Listeners.empty,
      Children.empty
    )
    Widget.empty(component)
  }

  override def element(
      namespace: String,
      name: String
  ): Widget[Nothing, Unit, Styles] = {
    val component = Component.Element(
      name,
      namespace.some,
      Attributes.Empty,
      Listeners.empty,
      Children.empty
    )
    Widget.empty(component)
  }

  override val fragment: Widget[Nothing, Unit, Styles] =
    Widget.empty(Component.Fragment(Children.empty))

  override def text(value: String): Widget[Nothing, Unit, Styles] =
    Widget.empty(Component.Text(value))
}

object StyledHtmlDsl extends StyledHtmlDsl

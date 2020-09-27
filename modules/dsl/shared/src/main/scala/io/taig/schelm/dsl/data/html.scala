package io.taig.schelm.dsl.data

import io.taig.schelm.css.data.Style
import io.taig.schelm.data.{Attributes, Children, Lifecycle, Listeners}

final case class Div[+Event, -Context](
    attributes: Attributes,
    listeners: Listeners[Event],
    style: Style,
    lifecycle: Lifecycle.Element,
    children: Children[DslWidget[Event, Context]]
) extends DslNode.Element.Normal[Event, Context]("div") {
  override def copy[A >: Event, B <: Context](
      attributes: Attributes,
      listeners: Listeners[A],
      style: Style,
      lifecycle: Lifecycle.Element,
      children: Children[DslWidget[A, B]]
  ): Div[A, B] = Div(attributes, listeners, style, lifecycle, children)
}

final case class Span[+Event, -Context](
    attributes: Attributes,
    listeners: Listeners[Event],
    style: Style,
    lifecycle: Lifecycle.Element,
    children: Children[DslWidget[Event, Context]]
) extends DslNode.Element.Normal[Event, Context]("span") {
  override def copy[A >: Event, B <: Context](
      attributes: Attributes,
      listeners: Listeners[A],
      style: Style,
      lifecycle: Lifecycle.Element,
      children: Children[DslWidget[A, B]]
  ): Span[A, B] = Span(attributes, listeners, style, lifecycle, children)
}

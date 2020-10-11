package io.taig.schelm.dsl.data

import io.taig.schelm.css.data.Style
import io.taig.schelm.data.{Attributes, Children, Lifecycle, Listeners}

final case class Div[F[_], +Event, -Context](
    attributes: Attributes,
    listeners: Listeners[F],
    style: Style,
    lifecycle: Lifecycle.Element[F],
    children: Children[DslWidget[F, Event, Context]]
) extends DslNode.Element.Normal[F, Event, Context]("div") {
  override def copy[A >: Event, B <: Context](
      attributes: Attributes,
      listeners: Listeners[F],
      style: Style,
      lifecycle: Lifecycle.Element[F],
      children: Children[DslWidget[F, A, B]]
  ): Div[F, A, B] = Div(attributes, listeners, style, lifecycle, children)
}

final case class Span[F[_], +Event, -Context](
    attributes: Attributes,
    listeners: Listeners[F],
    style: Style,
    lifecycle: Lifecycle.Element[F],
    children: Children[DslWidget[F, Event, Context]]
) extends DslNode.Element.Normal[F, Event, Context]("span") {
  override def copy[A >: Event, B <: Context](
      attributes: Attributes,
      listeners: Listeners[F],
      style: Style,
      lifecycle: Lifecycle.Element[F],
      children: Children[DslWidget[F, A, B]]
  ): Span[F, A, B] = Span(attributes, listeners, style, lifecycle, children)
}

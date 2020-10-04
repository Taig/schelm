package io.taig.schelm.dsl.data

import io.taig.schelm.css.data.Style
import io.taig.schelm.data.{Attributes, Children, Lifecycle, Listeners}

final case class Div[F[_], -Context](
    attributes: Attributes,
    listeners: Listeners[F],
    style: Style,
    lifecycle: Lifecycle.Element[F],
    children: Children[DslWidget[F, Context]]
) extends DslNode.Element.Normal[F, Context]("div") {
  override def copy[A <: Context](
      attributes: Attributes,
      listeners: Listeners[F],
      style: Style,
      lifecycle: Lifecycle.Element[F],
      children: Children[DslWidget[F, A]]
  ): Div[F, A] = Div(attributes, listeners, style, lifecycle, children)
}

final case class Span[F[_], -Context](
    attributes: Attributes,
    listeners: Listeners[F],
    style: Style,
    lifecycle: Lifecycle.Element[F],
    children: Children[DslWidget[F, Context]]
) extends DslNode.Element.Normal[F, Context]("span") {
  override def copy[A <: Context](
      attributes: Attributes,
      listeners: Listeners[F],
      style: Style,
      lifecycle: Lifecycle.Element[F],
      children: Children[DslWidget[F, A]]
  ): Span[F, A] = Span(attributes, listeners, style, lifecycle, children)
}

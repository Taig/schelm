package io.taig.schelm.dsl.keyword

import io.taig.schelm.css.data.{Css, Style}
import io.taig.schelm.data._
import io.taig.schelm.dsl.builder.{ElementNormalBuilder, ElementVoidBuilder}
import io.taig.schelm.dsl.data.{Button, Div, DslNode, Span}
import io.taig.schelm.redux.data.Redux

trait NodeKeyword {
  def element(name: String): ElementNormalBuilder = new ElementNormalBuilder(name)

  final def void(name: String): ElementVoidBuilder = new ElementVoidBuilder(name)

  final def fragment[F[_], Event, Context](
      children: Children[DslNode[F, Event, Context]] = Children.Empty
  ): DslNode[F, Event, Context] =
    DslNode.Pure(Redux.Pure(Widget.Pure(State.Stateless(Css(Node.Fragment(children), Style.Empty)))))

  final def text(value: String): DslNode[Nothing, Nothing, Any] =
    DslNode.Pure(
      Redux.Pure(Widget.Pure(State.Stateless(Css(Node.Text(value, Listeners.Empty, Lifecycle.Noop), Style.Empty))))
    )

  final def button[F[_], Event, Context](
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop,
      children: Children[DslNode[F, Event, Context]] = Children.Empty
  ): Button[F, Event, Context] = Button(attributes, listeners, style, lifecycle, children)

  final def div[F[_], Event, Context](
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop,
      children: Children[DslNode[F, Event, Context]] = Children.Empty
  ): Div[F, Event, Context] = Div(attributes, listeners, style, lifecycle, children)

  final def span[F[_], Event, Context](
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop,
      children: Children[DslNode[F, Event, Context]] = Children.Empty
  ): Span[F, Event, Context] = Span(attributes, listeners, style, lifecycle, children)

  final val br: ElementVoidBuilder = void("br")
  final val hr: ElementVoidBuilder = void("hr")
  final val header = element("header")
  final val i = element("i")
  final val p = element("p")
  final val section = element("section")
}

object NodeKeyword extends NodeKeyword

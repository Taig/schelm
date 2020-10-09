package io.taig.schelm.dsl.keyword

import io.taig.schelm.css.data.{Css, Style}
import io.taig.schelm.data._
import io.taig.schelm.dsl.builder.{ElementNormalBuilder, ElementVoidBuilder}
import io.taig.schelm.dsl.data.{AttributesOps, Div, DslWidget, Span}

trait NodeKeyword {
  def element(name: String): ElementNormalBuilder = new ElementNormalBuilder(name)

  final def void(name: String): ElementVoidBuilder = new ElementVoidBuilder(name)

  final def fragment[F[_], Context](
      children: Children[DslWidget[F, Context]] = Children.Empty
  ): DslWidget[F, Context] =
    DslWidget.Pure(Widget.Pure(State.Stateless(Css(Node.Fragment(children), Style.Empty))))

  final def text(value: String): DslWidget[Nothing, Any] =
    DslWidget.Pure(
      Widget.Pure(State.Stateless(Css(Node.Text(value, Listeners.Empty, Lifecycle.Noop), Style.Empty)))
    )

  final def div[F[_], Context](
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop,
      children: Children[DslWidget[F, Context]] = Children.Empty
  ): Div[F, Context] = Div(attributes, listeners, style, lifecycle, children)

  final def span[F[_], Context](
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop,
      children: Children[DslWidget[F, Context]] = Children.Empty
  ): Span[F, Context] = Span(attributes, listeners, style, lifecycle, children)

  final val br: ElementVoidBuilder = void("br")
  final val button = element("button")
  final val hr: ElementVoidBuilder = void("hr")
  final val header = element("header")
  final val i = element("i")
  final val p = element("p")
  final val section = element("section")
}

object NodeKeyword extends NodeKeyword

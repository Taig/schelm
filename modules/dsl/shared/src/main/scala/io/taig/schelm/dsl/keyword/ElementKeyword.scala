package io.taig.schelm.dsl.keyword

import io.taig.schelm.css.data.{CssNode, Style}
import io.taig.schelm.data._
import io.taig.schelm.dsl.builder.{ElementNormalBuilder, ElementVoidBuilder}
import io.taig.schelm.dsl.data.DslWidget

trait ElementKeyword {
  def element(name: String): ElementNormalBuilder = new ElementNormalBuilder(name)

  final def void(name: String): ElementVoidBuilder = new ElementVoidBuilder(name)

  final def fragment[Event, Context](
      lifecycle: Lifecycle[Callback.Fragment[Event]] = Lifecycle.Empty,
      children: Children[DslWidget[Event, Context]] = Children.Empty
  ): DslWidget[Event, Context] =
    DslWidget.Pure(Widget.Pure(CssNode(Node.Fragment(children, lifecycle), Style.Empty)))

  final def text(value: String): DslWidget[Nothing, Any] =
    DslWidget.Pure(Widget.Pure(CssNode(Node.Text(value, Listeners.Empty, Lifecycle.Empty), Style.Empty)))

  final val br: ElementVoidBuilder = void("br")
  final val button = element("button")
  final val div = element("div")
  final val hr: ElementVoidBuilder = void("hr")
  final val i = element("i")
  final val p = element("p")
  final val span = element("span")
}

object ElementKeyword extends ElementKeyword

package io.taig.schelm.dsl

import io.taig.schelm.css.data._
import io.taig.schelm.data._

trait ElementDsl {
  def element(name: String): DslWidget.Element.Normal[Any] = {
    val element = Component.Element(
      Tag(name, Attributes.Empty, Listeners.Empty),
      Component.Element.Type.Normal(Children.Empty),
      Lifecycle.Empty
    )

    DslWidget.Element.Normal(Widget.Pure(CssNode(element, Style.Empty)))
  }

  final val fragment: DslWidget.Fragment[Any] =
    DslWidget.Fragment(Widget.Pure(CssNode(Component.Fragment(Children.Empty, Lifecycle.Empty), Style.Empty)))

  final def text(value: String): DslWidget.Text[Any] =
    DslWidget.Text(Widget.Pure(CssNode(Component.Text(value, Listeners.Empty, Lifecycle.Empty), Style.Empty)))

  def void(tag: String): DslWidget.Element.Void[Any] = {
    val element =
      Component.Element(Tag(tag, Attributes.Empty, Listeners.Empty), Component.Element.Type.Void, Lifecycle.Empty)
    DslWidget.Element.Void(Widget.Pure(CssNode(element, Style.Empty)))
  }

  final val br: DslWidget.Element.Void[Any] = void("br")

  final val button: DslWidget.Element.Normal[Any] = element("button")

  final val div: DslWidget.Element.Normal[Any] = element("div")

  final val hr: DslWidget.Element.Void[Any] = void("hr")

  final val p: DslWidget.Element.Normal[Any] = element("p")

  final val span: DslWidget.Element.Normal[Any] = element("span")
}

package io.taig.schelm.dsl.keyword

import io.taig.schelm.css.data.{CssNode, Style}
import io.taig.schelm.data._
import io.taig.schelm.dsl.data.DslWidget

trait ElementKeyword {
  def element[A](
      name: String,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle[Callback.Element] = Lifecycle.Empty,
      children: Children[DslWidget[A]] = Children.Empty
  ): DslWidget[A] = {
    val element = Component.Element(
      Tag(name, attributes, listeners),
      Component.Element.Type.Normal(children),
      lifecycle
    )

    DslWidget.Pure(Widget.Pure(CssNode(element, style)))
  }

  final def fragment[A](lifecycle: Lifecycle[Callback.Fragment] = Lifecycle.Empty, children: Children[DslWidget[A]] = Children.Empty): DslWidget[A] =
    DslWidget.Pure(Widget.Pure(CssNode(Component.Fragment(children, lifecycle), Style.Empty)))

  final def text(value: String): DslWidget[Any] =
    DslWidget.Pure(Widget.Pure(CssNode(Component.Text(value, Listeners.Empty, Lifecycle.Empty), Style.Empty)))

  def void(tag: String, attributes: Attributes = Attributes.Empty,
           listeners: Listeners = Listeners.Empty,
           style: Style = Style.Empty,
           lifecycle: Lifecycle[Callback.Element] = Lifecycle.Empty): DslWidget[Any] = {
    val element = Component.Element(Tag(tag, attributes, listeners), Component.Element.Type.Void, lifecycle)
    DslWidget.Pure[Any](Widget.Pure(CssNode(element, style)))
  }

//  final val br: DslWidget.Element.Void[Any] = void("br")
//
//  final val button: DslWidget.Element.Normal[Any] = element("button")

  final def div[A](
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle[Callback.Element] = Lifecycle.Empty,
      children: Children[DslWidget[A]] = Children.Empty
  ): DslWidget[A] = element("div", attributes, listeners, style, lifecycle, children)

//  final val hr: DslWidget.Element.Void[Any] = void("hr")

  final def i[A](
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle[Callback.Element] = Lifecycle.Empty,
      children: Children[DslWidget[A]] = Children.Empty
  ): DslWidget[A] = element("i", attributes, listeners, style, lifecycle, children)

//  final val p: DslWidget.Element.Normal[Any] = element("p")

  final def span[A](
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle[Callback.Element] = Lifecycle.Empty,
      children: Children[DslWidget[A]] = Children.Empty
  ): DslWidget[A] = element("span", attributes, listeners, style, lifecycle, children)
}

object ElementKeyword extends ElementKeyword

package io.taig.schelm.dsl.builder

import io.taig.schelm.css.data.{CssNode, Style}
import io.taig.schelm.data.{Attributes, Callback, Children, Lifecycle, Listeners, Node, Tag, Widget}
import io.taig.schelm.dsl.data.DslWidget

final class ElementNormalBuilder(val name: String) extends AnyVal {
  def apply[A](
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle[Callback.Element] = Lifecycle.Empty,
      children: Children[DslWidget[A]] = Children.Empty
  ): DslWidget[A] = {
    val element = Node.Element(
      Tag(name, attributes, listeners),
      Node.Element.Type.Normal(children),
      lifecycle
    )

    DslWidget.Pure(Widget.Pure(CssNode(element, style)))
  }
}

package io.taig.schelm.dsl.builder

import io.taig.schelm.css.data.{CssNode, Style}
import io.taig.schelm.data.{Attributes, Callback, Children, Lifecycle, Listeners, Node, Tag, Widget}
import io.taig.schelm.dsl.data.DslWidget

final class ElementNormalBuilder(val name: String) extends AnyVal {
  def apply[Event, Context](
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[Event] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle[Callback.Element] = Lifecycle.Empty,
      children: Children[DslWidget[Event, Context]] = Children.Empty
  ): DslWidget[Event, Context] = {
    val element = Node.Element(
      Tag(name, attributes, listeners),
      Node.Element.Type.Normal(children),
      lifecycle
    )

    DslWidget.Pure(Widget.Pure(CssNode(element, style)))
  }
}

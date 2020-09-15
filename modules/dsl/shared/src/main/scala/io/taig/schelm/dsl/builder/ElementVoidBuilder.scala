package io.taig.schelm.dsl.builder

import io.taig.schelm.css.data.{CssNode, Style}
import io.taig.schelm.data._
import io.taig.schelm.dsl.data.DslWidget

final class ElementVoidBuilder(val name: String) extends AnyVal {
  def apply[A](
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle[Callback.Element] = Lifecycle.Empty
  ): DslWidget[A] = {
    val element = Node.Element(Tag(name, attributes, listeners), Node.Element.Type.Void, lifecycle)
    DslWidget.Pure(Widget.Pure(CssNode(element, style)))
  }
}

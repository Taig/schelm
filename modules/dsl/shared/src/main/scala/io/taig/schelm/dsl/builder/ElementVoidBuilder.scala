package io.taig.schelm.dsl.builder

import io.taig.schelm.css.data.{CssNode, Style}
import io.taig.schelm.data._
import io.taig.schelm.dsl.data.DslWidget

final class ElementVoidBuilder(val name: String) extends AnyVal {
  def apply[F[_], Context](
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): DslWidget[F, Context] = {
    val element = Node.Element(Tag(name, attributes, listeners), Node.Element.Variant.Void, lifecycle)
    DslWidget.Pure[F, Context](Widget.Pure(State.Stateless(CssNode(element, style))))
  }
}

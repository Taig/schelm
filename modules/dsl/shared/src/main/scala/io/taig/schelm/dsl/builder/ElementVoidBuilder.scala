package io.taig.schelm.dsl.builder

import io.taig.schelm.css.data.{Css, Style}
import io.taig.schelm.data._
import io.taig.schelm.dsl.data.DslNode
import io.taig.schelm.redux.data.Redux

final class ElementVoidBuilder(val name: String) extends AnyVal {
  def apply[F[_]](
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): DslNode[F, Nothing, Any] = {
    val element = Node.Element(Tag(name, attributes, listeners), Node.Element.Variant.Void, lifecycle)
    DslNode.Pure[F, Nothing, Any](Redux.Pure(Widget.Pure(State.Stateless(Css(element, style)))))
  }
}

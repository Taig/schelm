package io.taig.schelm.dsl.builder

import io.taig.schelm.css.data.{Css, Style}
import io.taig.schelm.data._
import io.taig.schelm.dsl.data.DslWidget
import io.taig.schelm.redux.data.Redux

final class ElementNormalBuilder(val name: String) extends AnyVal {
  def apply[F[_], Event, Context](
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop,
      children: Children[DslWidget[F, Event, Context]] = Children.Empty
  ): DslWidget[F, Event, Context] = {
    val element = Node.Element(
      Tag(name, attributes, listeners),
      Node.Element.Variant.Normal(children),
      lifecycle
    )

    DslWidget.Pure(Redux.Pure(Widget.Pure(State.Stateless(Css(element, style)))))
  }
}

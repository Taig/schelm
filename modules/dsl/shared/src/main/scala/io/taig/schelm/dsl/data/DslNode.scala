package io.taig.schelm.dsl.data

import io.taig.schelm.css.data.{Css, Style}
import io.taig.schelm.data._
import io.taig.schelm.redux.data.Redux

object DslNode {
  abstract class Element[F[_], +Event, -Context](val name: String) extends DslWidget.Component[F, Event, Context] {
    def attributes: Attributes

    def listeners: Listeners[F]

    def style: Style

    def lifecycle: Lifecycle.Element[F]

    def copy[A >: Event, B <: Context](
        attributes: Attributes = this.attributes,
        listeners: Listeners[F] = this.listeners,
        style: Style = Style.Empty,
        lifecycle: Lifecycle.Element[F] = this.lifecycle,
        children: Children[DslWidget[F, A, B]] = Children.empty[DslWidget[F, A, Context]]
    ): DslNode.Element[F, A, B]
  }

  object Element {
    abstract class Normal[F[_], +Event, -Context](name: String) extends DslNode.Element[F, Event, Context](name) {
      def children: Children[DslWidget[F, Event, Context]]

      override def copy[A >: Event, B <: Context](
          attributes: Attributes = this.attributes,
          listeners: Listeners[F] = this.listeners,
          style: Style = Style.Empty,
          lifecycle: Lifecycle.Element[F] = this.lifecycle,
          children: Children[DslWidget[F, A, B]] = this.children
      ): Element[F, A, B]

      final override def render: DslWidget[F, Event, Context] = {
        val element = Node.Element(
          Tag(name, attributes, listeners),
          Node.Element.Variant.Normal(children),
          lifecycle
        )

        DslWidget.Pure(Redux.Pure(Widget.Pure(State.Stateless(Css(element, style)))))
      }
    }
  }
}

package io.taig.schelm.dsl.data

import io.taig.schelm.css.data.{CssNode, Style}
import io.taig.schelm.data._

object DslNode {
  abstract class Element[+Event, -Context](val name: String) extends DslWidget.Component[Event, Context] {
    def attributes: Attributes

    def listeners: Listeners[Event]

    def style: Style

    def lifecycle: Lifecycle.Element

    def copy[A >: Event, B <: Context](
        attributes: Attributes = this.attributes,
        listeners: Listeners[A] = this.listeners,
        style: Style = Style.Empty,
        lifecycle: Lifecycle.Element = Lifecycle.Element.noop,
        children: Children[DslWidget[A, B]] = Children.empty[DslWidget[Event, Context]]
    ): DslNode.Element[A, B]
  }

  object Element {
    abstract class Normal[+Event, -Context](name: String) extends DslNode.Element[Event, Context](name) {
      def children: Children[DslWidget[Event, Context]]

      override def copy[A >: Event, B <: Context](
          attributes: Attributes = this.attributes,
          listeners: Listeners[A] = this.listeners,
          style: Style = Style.Empty,
          lifecycle: Lifecycle.Element = Lifecycle.Element.noop,
          children: Children[DslWidget[A, B]] = this.children
      ): Element[A, B]

      final override def render: DslWidget[Event, Context] = {
        val element = Node.Element(
          Tag(name, attributes, listeners),
          Node.Element.Type.Normal(children),
          lifecycle
        )

        DslWidget.Pure(Widget.Pure(CssNode(element, style)))
      }
    }
  }
}

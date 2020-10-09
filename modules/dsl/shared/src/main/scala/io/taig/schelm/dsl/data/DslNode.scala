package io.taig.schelm.dsl.data

import io.taig.schelm.css.data.{Css, Style}
import io.taig.schelm.data._

object DslNode {
  abstract class Element[F[_], -Context](val name: String) extends DslWidget.Component[F, Context] {
    def attributes: Attributes

    def listeners: Listeners[F]

    def style: Style

    def lifecycle: Lifecycle.Element[F]

    def copy[B <: Context](
        attributes: Attributes = this.attributes,
        listeners: Listeners[F] = this.listeners,
        style: Style = Style.Empty,
        lifecycle: Lifecycle.Element[F] = this.lifecycle,
        children: Children[DslWidget[F, B]] = Children.empty[DslWidget[F, Context]]
    ): DslNode.Element[F, B]
  }

  object Element {
    abstract class Normal[F[_], -Context](name: String) extends DslNode.Element[F, Context](name) {
      def children: Children[DslWidget[F, Context]]

      override def copy[B <: Context](
          attributes: Attributes = this.attributes,
          listeners: Listeners[F] = this.listeners,
          style: Style = Style.Empty,
          lifecycle: Lifecycle.Element[F] = this.lifecycle,
          children: Children[DslWidget[F, B]] = this.children
      ): Element[F, B]

      final override def render: DslWidget[F, Context] = {
        val element = Node.Element(
          Tag(name, attributes, listeners),
          Node.Element.Variant.Normal(children),
          lifecycle
        )

        DslWidget.Pure(Widget.Pure(State.Stateless(Css(element, style))))
      }
    }
  }
}

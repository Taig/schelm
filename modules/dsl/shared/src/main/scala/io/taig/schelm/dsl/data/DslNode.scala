package io.taig.schelm.dsl.data

import io.taig.schelm.css.data.{Css, Style}
import io.taig.schelm.data.{Attributes, Children, Lifecycle, Listeners, Node, State, Tag, Widget}
import io.taig.schelm.redux.data.Redux

sealed abstract class DslNode[+F[_], +Event, -Context] extends Product with Serializable

object DslNode {
  final case class Pure[F[_], Event, Context](
      redux: Redux[F, Event, Widget[Context, State[F, Css[Node[F, Listeners[F], DslNode[F, Event, Context]]]]]]
  ) extends DslNode[F, Event, Context]

  abstract class Component[+F[_], +Event, -Context] extends DslNode[F, Event, Context] {
    def render: DslNode[F, Event, Context]
  }

  abstract class Element[F[_], +Event, -Context](val name: String) extends DslNode.Component[F, Event, Context] {
    def attributes: Attributes

    def listeners: Listeners[F]

    def style: Style

    def lifecycle: Lifecycle.Element[F]

    def copy[A >: Event, B <: Context](
        attributes: Attributes = this.attributes,
        listeners: Listeners[F] = this.listeners,
        style: Style = Style.Empty,
        lifecycle: Lifecycle.Element[F] = this.lifecycle,
        children: Children[DslNode[F, A, B]] = Children.empty[DslNode[F, A, Context]]
    ): Element[F, A, B]
  }

  object Element {
    abstract class Normal[F[_], +Event, -Context](name: String) extends Element[F, Event, Context](name) {
      def children: Children[DslNode[F, Event, Context]]

      override def copy[A >: Event, B <: Context](
          attributes: Attributes = this.attributes,
          listeners: Listeners[F] = this.listeners,
          style: Style = Style.Empty,
          lifecycle: Lifecycle.Element[F] = this.lifecycle,
          children: Children[DslNode[F, A, B]] = this.children
      ): Element[F, A, B]

      final override def render: DslNode[F, Event, Context] = {
        val element = Node.Element(
          Tag(name, attributes, listeners),
          Node.Element.Variant.Normal(children),
          lifecycle
        )

        DslNode.Pure(Redux.Pure(Widget.Pure(State.Stateless(Css(element, style)))))
      }
    }
  }

  def run[F[_], Event, Context]: DslNode[F, Event, Context] => Redux[F, Event, Widget[Context, State[F, Css[
    Node[F, Listeners[F], DslNode[F, Event, Context]]
  ]]]] = {
    case widget: Pure[F, Event, Context]      => widget.redux
    case widget: Component[F, Event, Context] => run(widget.render)
  }
}

package io.taig

import cats.implicits._

package object schelm {
  type Html[+Event] = Fix[Component[+?, Event]]

  object Html {
    def apply[Event](component: Component[Html[Event], Event]): Html[Event] =
      Fix[Component[+?, Event]](component)
  }

  implicit final class HtmlSyntax[A](html: Html[A])
      extends ComponentOps[Html, A](
        html,
        _.value,
        (component, _) => Html(component)
      )

  type Reference[+Event, Node] = Cofree[Component[+?, Event], Option[Node]]

  object Reference {
    def apply[A, B](
        component: Component[Reference[A, B], A],
        node: Option[B]
    ): Reference[A, B] =
      Cofree[Component[+?, A], Option[B]](node, component)
  }

  implicit final class ReferenceSyntax[A, Node](reference: Reference[A, Node])
      extends ComponentOps[Reference[?, Node], A](
        reference,
        _.tail,
        (component, node) => Reference(component, node.head)
      ) {
    def root: List[Node] = (reference.head, reference.tail) match {
      case (Some(node), _) => List(node)
      case (None, component: Component.Fragment[Reference[A, Node]]) =>
        component.children.values.flatMap(_.root)
      case (None, _) => List.empty
    }
  }

  def toHtml[Event, A](value: Cofree[Component[+?, Event], A]): Html[Event] =
    value.tail match {
      case Component.Element(name, attributes, children) =>
        Html(
          Component.Element(
            name,
            attributes,
            children.map((_, value) => toHtml(value))
          )
        )
      case Component.Fragment(children) =>
        Html(Component.Fragment(children.map((_, value) => toHtml(value))))
      case Component.Lazy(component, hash) =>
        Html(Component.Lazy(component.map(toHtml[Event, A]), hash))
      case component: Component.Text => Html(component)
    }

  type CommandHandler[F[_], Command, Event] = Command => F[Option[Event]]

  type EventHandler[State, Event, Command] =
    (State, Event) => Result[State, Command]
}

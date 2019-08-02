package io.taig

package object schelm {
  final case class Fix[+F[+_]](value: F[Fix[F]])
  final case class Cofree[+F[+_], +A](head: A, tail: F[Cofree[F, A]])

  type Html[+A] = Fix[Component[+?, A]]

  object Html {
    def apply[A](component: Component[Html[A], A]): Html[A] =
      Fix[Component[+?, A]](component)
  }

  implicit final class HtmlSyntax[A](html: Html[A])
      extends ComponentOps[Html, A](
        html,
        _.value,
        (component, _) => Html(component)
      )

  type Node[+A, B] = Cofree[Component[+?, A], Option[B]]

  object Node {
    def apply[A, B](
        component: Component[Node[A, B], A],
        node: Option[B]
    ): Node[A, B] =
      Cofree[Component[+?, A], Option[B]](node, component)
  }

  implicit final class NodeSyntax[A, B](node: Node[A, B])
      extends ComponentOps[Node[?, B], A](
        node,
        _.tail,
        (component, node) => Node(component, node.head)
      ) {
    def root: List[B] = (node.head, node.tail) match {
      case (Some(node), _) => List(node)
      case (None, component: Component.Fragment[Node[A, B], A]) =>
        component.children.values.flatMap(_.root)
      case (None, _) => List.empty
    }
  }

  type CommandHandler[F[_], Command, Event] = Command => F[Option[Event]]

  type EventHandler[State, Event, Command] =
    (State, Event) => Result[State, Command]
}

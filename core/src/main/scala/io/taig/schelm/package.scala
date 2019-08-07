package io.taig

package object schelm extends internal.Nodes {
  type Node
  type Element <: Node
  type Text <: Text

  type CommandHandler[F[_], Command, Event] = Command => F[Option[Event]]

  type EventHandler[State, Event, Command] =
    (State, Event) => Result[State, Command]

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

  type Document[+Event, A] = Cofree[Component[+?, Event], A]

  object Document {
    def apply[Event, A](
        component: Component[Document[Event, A], Event],
        value: A
    ): Document[Event, A] = Cofree[Component[+?, Event], A](value, component)
  }

  def toHtml[Event](value: Document[Event, _]): Html[Event] =
    value.tail match {
      case component: Component.Element[Document[Event, _], Event] =>
        val children = component.children.map((_, child) => toHtml(child))
        Html(
          Component.Element(
            component.name,
            component.attributes,
            component.listeners,
            children
          )
        )
      case component: Component.Fragment[Document[Event, _]] =>
        val children = component.children.map((_, child) => toHtml(child))
        Html(Component.Fragment(children))
      case component: Component.Lazy[Document[Event, _]] =>
        Html(component.copy(eval = component.eval.map(toHtml)))
      case component: Component.Text => Html(component)
    }
}

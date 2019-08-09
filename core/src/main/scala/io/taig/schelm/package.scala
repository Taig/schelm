package io.taig

package object schelm extends internal.Nodes {
  type Node
  type Element <: Node
  type Text <: Text

  type CommandHandler[F[_], Command, Event] = Command => F[Option[Event]]

  type EventHandler[State, Event, Command] =
    (State, Event) => Result[State, Command]

  val unit: Unit = ()

  def toHtml[A](widget: Widget[A, Unit, _]): Html[A] =
    widget.component match {
      case component: Component.Element[Widget[A, Unit, _], A] =>
        val children = component.children.map((_, child) => toHtml[A](child))
        Html(
          Component.Element(
            component.name,
            component.attributes,
            component.listeners,
            children
          )
        )
      case component: Component.Fragment[Widget[A, Unit, _]] =>
        val children = component.children.map((_, child) => toHtml[A](child))
        Html(Component.Fragment(children))
      case component: Component.Lazy[Widget[A, Unit, _]] =>
        Html(component.copy(eval = component.eval.map(toHtml[A])))
      case component: Component.Text => Html(component)
    }
}

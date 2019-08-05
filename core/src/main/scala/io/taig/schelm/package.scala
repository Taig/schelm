package io.taig

import cats.implicits._

package object schelm {
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

  def toHtml[Event, A](value: Cofree[Component[+?, Event], A]): Html[A] =
    value.tail match {
      case Component.Element(name, attributes, children) => ???
      case Component.Fragment(children) => ???
      case Component.Lazy(eval, hash) => ???
      case component: Component.Text => Html(component)
    }
}

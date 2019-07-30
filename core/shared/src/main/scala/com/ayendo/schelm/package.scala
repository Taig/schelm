package com.ayendo

package object schelm {
  final case class Fix[F[_]](value: F[Fix[F]])
  final case class Cofree[F[_], A](head: A, tail: F[Cofree[F, A]])

  type Html[A] = Fix[Component[?, A]]

  object Html {
    def apply[A](component: Component[Html[A], A]): Html[A] =
      Fix[Component[?, A]](component)
  }

  implicit final class HtmlSyntax[A](html: Html[A])
      extends ComponentOps[Html, A](
        html,
        _.value,
        (component, _) => Html(component)
      )

  type CommandHandler[F[_], Command, Event] = Command => F[Option[Event]]

  type EventHandler[State, Event, Command] =
    (State, Event) => Result[State, Command]
}

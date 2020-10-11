package io.taig.schelm.dsl.data

import io.taig.schelm.css.data.Css
import io.taig.schelm.data.{Listeners, Node, State, Widget}
import io.taig.schelm.redux.data.Redux

sealed abstract class DslWidget[+F[_], +Event, -Context] extends Product with Serializable

object DslWidget {
  final case class Pure[F[_], Event, Context](
      widget: Redux[F, Event, Widget[Context, State[F, Css[Node[F, Listeners[F], DslWidget[F, Event, Context]]]]]]
  ) extends DslWidget[F, Event, Context]

  abstract class Component[+F[_], +Event, -Context] extends DslWidget[F, Event, Context] {
    def render: DslWidget[F, Event, Context]
  }
}

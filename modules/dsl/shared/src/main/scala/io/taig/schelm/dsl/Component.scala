package io.taig.schelm.dsl

import io.taig.schelm.css.data.Css
import io.taig.schelm.data.{Listeners, Node, State, Widget}
import io.taig.schelm.redux.data.Redux

sealed abstract class Component[+F[_], +Event, -Context] {
  def render: Redux[F, Event, Widget[Context, State[F, Css[Node[F, Listeners[F], Component[F, Event, Context]]]]]]
}

final case class Fragment[+F[_], +Event, -Context](
    render: Redux[F, Event, Widget[Context, State[F, Css[Node.Fragment[Component[F, Event, Context]]]]]]
) extends Component[F, Event, Context]

final case class Text[+F[_], +Event, -Context](
    render: Redux[F, Event, Widget[Context, State[F, Css[Node.Text[F, Listeners[F]]]]]]
) extends Component[F, Event, Context]

final case class Element[+F[_], +Event, -Context](
    render: Redux[F, Event, Widget[Context, State[F, Css[Node.Element[F, Listeners[F], Component[F, Event, Context]]]]]]
) extends Component[F, Event, Context]

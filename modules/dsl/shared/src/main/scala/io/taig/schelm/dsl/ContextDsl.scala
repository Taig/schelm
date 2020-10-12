package io.taig.schelm.dsl

import io.taig.schelm.css.data.{Css, Style}
import io.taig.schelm.data.{Children, Node, State, Widget}
import io.taig.schelm.dsl.data.DslNode
import io.taig.schelm.redux.algebra.EventManager
import io.taig.schelm.redux.data.Redux

trait ContextDsl {
  final def contextual[F[_], Event, Context](f: Context => DslNode[F, Event, Context]): DslNode[F, Event, Context] =
    DslNode.Pure(Redux.Render { (events: EventManager[F, Event]) =>
      Widget.Render((context: Context) => Widget.run(context)(Redux.run(events)(DslNode.run(f(context)))))
    })

  final def redux[F[_], Event, Context](
      f: EventManager[F, Event] => DslNode[F, Event, Context]
  ): DslNode[F, Event, Context] =
    DslNode.Pure(Redux.Render((events: EventManager[F, Event]) => Redux.run(events)(DslNode.run(f(events)))))

  final def stateful[F[_]]: StatefulApply[F] = new StatefulApply[F]

  final class StatefulApply[F[_]] {
    def apply[A, Event, Context](initial: A)(f: ((A => A) => F[Unit], A) => DslNode[F, Event, Context]): DslNode[F, Event, Context] =
      DslNode.Pure(
        Redux.Pure(Widget.Pure(State.Stateful(initial, {
          (update: (A => A) => F[Unit], current: A) => Css(Node.Fragment(Children.of(f(update, current))), Style.Empty)
        })))
      )
  }
}

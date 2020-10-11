package io.taig.schelm.dsl

import io.taig.schelm.data.Widget
import io.taig.schelm.dsl.data.DslNode
import io.taig.schelm.redux.algebra.EventManager
import io.taig.schelm.redux.data.Redux

trait ContextDsl {
  final def contextual[F[_], Event, Context](f: Context => DslNode[F, Event, Context]): DslNode[F, Event, Context] =
    DslNode.Pure(Redux.Render { (events: EventManager[F, Event]) =>
      Widget.Render { (context: Context) => Widget.run(context)(Redux.run(events)(DslNode.run(f(context)))) }
    })

  final def redux[F[_], Event, Context](
      f: EventManager[F, Event] => DslNode[F, Event, Context]
  ): DslNode[F, Event, Context] =
    DslNode.Pure(Redux.Render { (events: EventManager[F, Event]) => Redux.run(events)(DslNode.run(f(events))) })
}

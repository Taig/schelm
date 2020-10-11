package io.taig.schelm.dsl

import io.taig.schelm.data.Widget
import io.taig.schelm.dsl.data.DslWidget
import io.taig.schelm.redux.algebra.EventManager
import io.taig.schelm.redux.data.Redux

trait ContextDsl {
  final def contextual[F[_], Event, Context](f: Context => DslWidget[F, Event, Context]): DslWidget[F, Event, Context] =
    DslWidget.Pure(Redux.Render { (events: EventManager[F, Event]) =>
      Widget.Render { (context: Context) => Widget.run(context)(Redux.run(events)(DslWidget.run(f(context)))) }
    })

  final def redux[F[_], Event, Context](
      f: EventManager[F, Event] => DslWidget[F, Event, Context]
  ): DslWidget[F, Event, Context] =
    DslWidget.Pure(Redux.Render { (events: EventManager[F, Event]) => Redux.run(events)(DslWidget.run(f(events))) })
}

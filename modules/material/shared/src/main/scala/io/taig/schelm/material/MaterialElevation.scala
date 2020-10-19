package io.taig.schelm.material

import io.taig.schelm.dsl.Component

object MaterialElevation {
  def apply[F[_], Event, Context](component: Component[F, Event, Context]): Component[F, Event, Context] =
    component
}

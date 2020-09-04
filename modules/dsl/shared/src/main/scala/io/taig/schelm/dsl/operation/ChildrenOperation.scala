package io.taig.schelm.dsl.operation

import io.taig.schelm.css.data.StylesheetWidget
import io.taig.schelm.data.Children
import io.taig.schelm.dsl.data.DslWidget

abstract class ChildrenOperation[F[+_], Event, Context] {
  final def set(children: Children[StylesheetWidget[Event, Context]]): DslWidget[F, Event, Context] =
    patch(_ => children)

  def patch(
      f: Children[StylesheetWidget[Event, Context]] => Children[StylesheetWidget[Event, Context]]
  ): DslWidget[F, Event, Context]
}

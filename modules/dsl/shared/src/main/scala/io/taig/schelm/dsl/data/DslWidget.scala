package io.taig.schelm.dsl.data

import io.taig.schelm.css.data.{StylesheetNode, StylesheetWidget}
import io.taig.schelm.data.{Node, Widget}

final case class DslWidget[+F[+_], +Event, -Context](
    widget: Widget[Context, StylesheetNode[Event, F[StylesheetWidget[Event, Context]]]]
) extends AnyVal

object DslWidget {
  def toStylesheetWidget[Event, Context](
      widget: DslWidget[Node[Event, +*], Event, Context]
  ): StylesheetWidget[Event, Context] =
    StylesheetWidget(widget.widget)
}

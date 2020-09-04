package io.taig.schelm.css.data

import cats.implicits._
import io.taig.schelm.css.data
import io.taig.schelm.data.{Node, Widget}

final case class StylesheetWidget[+F[+_], +Event, -Context](
    widget: Widget[
      StylesheetNode[F[StylesheetWidget[F, Event, Context]], Node[Event, StylesheetWidget[F, Event, Context]], Event],
      Context
    ]
)

object StylesheetWidget {
  def toStylesheetHtml[Event, Context](
      widget: StylesheetWidget[Node[Event, +*], Event, Context],
      context: Context
  ): StylesheetHtml[Node[Event, +*], Event] =
    ???
}

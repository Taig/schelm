package io.taig.schelm.css.data

import io.taig.schelm.data.Element

sealed abstract class StylesheetNode[+A, +B, +Event] extends Product with Serializable

object StylesheetNode {
  final case class Styled[+A <: Element[Event, B], +B, +Event](element: A, styles: Stylesheet)
      extends StylesheetNode[A, B, Event]

  final case class Unstyled[+A, +Event](node: A) extends StylesheetNode[Nothing, A, Event]
}

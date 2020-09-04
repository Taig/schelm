package io.taig.schelm.css.data

import cats.implicits._
import io.taig.schelm.data.{Html, Node}

final case class StylesheetHtml[+Event](node: StylesheetNode[Event, Node[Event, StylesheetHtml[Event]]]) extends AnyVal

object StylesheetHtml {
  def toHtml[Event](html: StylesheetHtml[Event]): (Html[Event], Stylesheet) = {
    val result = html.node.value.map(toHtml[Event])
    val stylesheet = result.foldl(html.node.stylesheet) { case (left, (_, right)) => left |+| right }
    (Html(result.map { case (html, _) => html }), stylesheet)
  }
}

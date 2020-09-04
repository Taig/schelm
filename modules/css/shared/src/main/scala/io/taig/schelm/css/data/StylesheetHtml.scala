package io.taig.schelm.css.data

import io.taig.schelm.data.{Element, Html, Node}
import cats.implicits._

final case class StylesheetHtml[F[+_], +Event](
    node: StylesheetNode[F[StylesheetHtml[F, Event]], Node[Event, StylesheetHtml[F, Event]], Event]
) extends AnyVal

object StylesheetHtml {
  def toHtml[Event](html: StylesheetHtml[Node[Event, +*], Event]): (Html[Event], Stylesheet) =
    html.node match {
      case StylesheetNode.Styled(element @ Element.Normal(_, _), styles) =>
        val styled = element.children.map(toHtml[Event])
        val stylesheet = styled.foldl(styles) { case (left, (_, right)) => left |+| right }
        val html = Html(element.copy(children = styled.map { case (html, _) => html }))
        (html, stylesheet)
      case StylesheetNode.Styled(element @ Element.Void(_), styles) => (Html(element), styles)
      case StylesheetNode.Unstyled(node) =>
        val styled = node.map(toHtml[Event])
        val stylesheet = styled.foldl(Stylesheet.Empty) { case (left, (_, right)) => left |+| right }
        val html = Html(styled.map { case (html, _) => html })
        (html, stylesheet)
    }
}

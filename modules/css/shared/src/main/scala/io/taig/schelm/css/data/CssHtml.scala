package io.taig.schelm.css.data

import cats.implicits._
import io.taig.schelm.data.{Attribute, Html, Listeners, Node}

final case class CssHtml[F[_]](node: CssNode[Node[F, Listeners[F], CssHtml[F]]]) extends AnyVal

object CssHtml {
  private val EmptyStyles: Map[Selector, Style] = Map.empty

  def toHtml[F[_]](css: CssHtml[F]): (Html[F], Map[Selector, Style]) = {
    val nodes = css.node.map(_.map(toHtml[F]))

    val (CssNode(node, _), rules) = nodes match {
      case CssNode(node: Node.Element[F, Listeners[F], (Html[F], Map[Selector, Style])], style) if !style.isEmpty =>
        val identifier = Identifier(style.hashCode)
        val selector = identifier.toSelector
        val attribute = Attribute(Attribute.Key.Class, Attribute.Value(identifier.toClass))
        val update = CssNode(node.copy(tag = node.tag.copy(attributes = node.tag.attributes + attribute)), style)
        (update, Map(selector -> style))
      case node => (node, EmptyStyles)
    }

    val html = Html(node.map { case (html, _) => html })
    val styles = nodes.node.foldl(rules) { case (left, (_, right)) => left ++ right }

    (html, styles)
  }
}

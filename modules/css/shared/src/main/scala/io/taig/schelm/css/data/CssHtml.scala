package io.taig.schelm.css.data

import cats.implicits._
import io.taig.schelm.data.{Attribute, Component, Html}

final case class CssHtml(node: CssNode[Component[CssHtml]]) extends AnyVal

object CssHtml {
  private val EmptyStyles: Map[Selector, Style] = Map.empty

  def toHtml(css: CssHtml): (Html, Map[Selector, Style]) = {
    val nodes = css.node.map(_.map(toHtml))

    val (node, rules) = nodes match {
      case CssNode(component: Component.Element[(Html, Map[Selector, Style])], style) if !style.isEmpty =>
        val identifier = Identifier(style.hashCode)
        val selector = identifier.toSelector
        val attribute = Attribute(Attribute.Key.Class, Attribute.Value(identifier.toClass))
        val update =
          CssNode(component.copy(tag = component.tag.copy(attributes = component.tag.attributes + attribute)), style)
        (update, Map(selector -> style))
      case node => (node, EmptyStyles)
    }

    val html = Html(node.component.map { case (html, _) => html })
    val styles = nodes.component.foldl(rules) { case (left, (_, right)) => left ++ right }

    (html, styles)
  }
}

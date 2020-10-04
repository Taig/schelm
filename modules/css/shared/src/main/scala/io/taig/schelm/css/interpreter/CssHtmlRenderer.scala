package io.taig.schelm.css.interpreter

import cats.implicits._
import cats.{Applicative, Id, Monad}
import io.taig.schelm.algebra.{Dom, Renderer}
import io.taig.schelm.css.data.{CssHtml, CssNode, Identifier, Selector, Style}
import io.taig.schelm.data.{Attribute, Html, HtmlReference, Listeners, Node}
import io.taig.schelm.interpreter.HtmlRenderer

final class CssHtmlRenderer[F[_]: Applicative] extends Renderer[Id, CssHtml[F], (Html[F], Map[Selector, Style])] {
  val EmptyStyles: Map[Selector, Style] = Map.empty

  override def render(css: CssHtml[F]): (Html[F], Map[Selector, Style]) = {
    val nodes = css.node.map(_.map(render))

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

object CssHtmlRenderer {
  def apply[F[_]: Applicative]: Renderer[Id, CssHtml[F], (Html[F], Map[Selector, Style])] =
    new CssHtmlRenderer[F]
}

package io.taig.schelm.css.interpreter

import cats.implicits._
import cats.{Applicative, Id}
import io.taig.schelm.algebra.Renderer
import io.taig.schelm.css.data._
import io.taig.schelm.data.{Attribute, Html, Node}

final class CssHtmlRenderer[F[_]: Applicative] extends Renderer[Id, CssHtml[F], StyledHtml[F]] {
  override def render(css: CssHtml[F]): StyledHtml[F] = {
    val nodes = css.node.map(_.map(render))

    val (rules, CssNode(node, _)) = nodes match {
      case CssNode(element @ Node.Element(tag, _, _), style) if !style.isEmpty =>
        val identifier = Identifier(style.hashCode)
        val selector = identifier.toSelector
        val attribute = Attribute(Attribute.Key.Class, Attribute.Value(identifier.toClass))
        val node = CssNode(element.copy(tag = tag.copy(attributes = tag.attributes + attribute)), style)
        Map(selector -> style) -> node
      case node => CssHtmlRenderer.EmptyStyles -> node
    }

    val html = Html(node.map(_.html))
    val styles = nodes.node.map(_.styles).foldl(rules)(_ ++ _)

    StyledHtml(styles, html)
  }
}

object CssHtmlRenderer {
  private val EmptyStyles: Map[Selector, Style] = Map.empty

  def apply[F[_]: Applicative]: Renderer[Id, CssHtml[F], StyledHtml[F]] = new CssHtmlRenderer[F]
}

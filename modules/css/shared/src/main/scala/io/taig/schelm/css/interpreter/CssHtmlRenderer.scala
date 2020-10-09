package io.taig.schelm.css.interpreter

import cats.implicits._
import cats.{Applicative, Id}
import io.taig.schelm.algebra.Renderer
import io.taig.schelm.css.data._
import io.taig.schelm.data.{Attribute, Html, Node}
import io.taig.schelm.util.FunctionKs

final class CssHtmlRenderer[F[_]] extends Renderer[Id, CssHtml[F], StyledHtml[F]] {
  override def render(css: CssHtml[F]): StyledHtml[F] = {
    val styles = collection.mutable.HashMap.empty[Selector, Style]
    val html = render(css, styles)
    StyledHtml[F](styles.toMap, html)
  }

  def render(css: CssHtml[F], styles: collection.mutable.HashMap[Selector, Style]): Html[F] =
    css.unfix.node match {
      case element @ Node.Element(tag, _, _) if !css.unfix.style.isEmpty =>
        val identifier = Identifier(css.unfix.style.hashCode)
        val selector = identifier.toSelector
        val attribute = Attribute(Attribute.Key.Class, Attribute.Value(identifier.toClass))
        val node = element.copy(tag = tag.copy(attributes = tag.attributes + attribute))
        styles += selector -> css.unfix.style
        Html(node.map(node => render(node, styles)))
      case node => Html(node.map(node => render(node, styles)))
    }
}

object CssHtmlRenderer {
  def apply[F[_]: Applicative]: Renderer[F, CssHtml[F], StyledHtml[F]] =
    new CssHtmlRenderer[F].mapK(FunctionKs.liftId[F])
}

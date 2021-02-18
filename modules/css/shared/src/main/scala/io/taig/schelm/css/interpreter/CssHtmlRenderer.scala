package io.taig.schelm.css.interpreter

import cats.arrow.FunctionK
import cats.syntax.all._
import cats.{Applicative, Id => Identity}
import io.taig.schelm.algebra.Renderer
import io.taig.schelm.css.data._
import io.taig.schelm.data.{Attribute, Html, Node}

final class CssHtmlRenderer[F[_]] extends Renderer[Identity, CssHtml[F], (Stylesheet, Html[F])] {
  override def render(css: CssHtml[F]): (Stylesheet, Html[F]) = {
    val rules = styles(css.unfix).flatMap { style =>
      val selectors = Selectors.of(Selector(s"._${Integer.toHexString(style.hashCode)}"))
      style.toRules(selectors)
    }

    (Stylesheet.from(rules), html(css.unfix))
  }

  def html(css: Css[Node[F, CssHtml[F]]]): Html[F] = {
    val node =
      if (css.style.isEmpty) css.value
      else
        css.value match {
          case node: Node.Element[F, CssHtml[F]] =>
            val value = Attribute.Value(s"_${Integer.toHexString(css.style.hashCode)}")
            val attributes = node.tag.attributes + Attribute(Attribute.Key.Class, value)
            val tag = node.tag.copy(attributes = attributes)
            node.copy(tag = tag)
          case node => node
        }

    Html(node.map(css => html(css.unfix)))
  }

  def styles(css: Css[Node[F, CssHtml[F]]]): Set[Style] = {
    val start = if (css.style.isEmpty) Set.empty[Style] else Set(css.style)
    css.value.foldl(start)((result, css) => result ++ styles(css.unfix))
  }
}

object CssHtmlRenderer {
  def apply[F[_]](implicit F: Applicative[F]): Renderer[F, CssHtml[F], (Stylesheet, Html[F])] =
    new CssHtmlRenderer[F].mapK(FunctionK.liftFunction[Identity, F](F.pure))
}

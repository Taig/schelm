package io.taig.schelm.css.interpreter

import cats.Applicative
import cats.data.Kleisli
import cats.implicits._
import io.taig.schelm.algebra.Renderer
import io.taig.schelm.css.data._
import io.taig.schelm.data.{Attribute, Html, Listeners, Node}
import org.scalajs.dom.raw.Event

object CssHtmlRenderer {
  def apply[F[_]: Applicative]: Renderer[F, CssHtml[F], StyledHtml[F]] = {
    def render(css: CssHtml[F], styles: collection.mutable.HashMap[Selector, Style]): Html[F] =
      css.unfix.value match {
        case element: Node.Element[F, Listeners[F], CssHtml[F]] if !css.unfix.style.isEmpty =>
          val identifier = Identifier(css.unfix.style.hashCode)
          val selector = identifier.toSelector
          val attribute = Attribute(Attribute.Key.Class, Attribute.Value(identifier.toClass))
          val node = element.copy(tag = element.tag.copy(attributes = element.tag.attributes + attribute))
          styles += selector -> css.unfix.style
          Html(node.map(node => render(node, styles)))
        case node => Html(node.map(node => render(node, styles)))
      }

    Kleisli { css =>
      val styles = collection.mutable.HashMap.empty[Selector, Style]
      val html = render(css, styles)
      StyledHtml[F](styles.toMap, html).pure[F]
    }
  }
}

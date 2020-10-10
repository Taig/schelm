package io.taig.schelm.css.interpreter

import cats.data.{Ior, Kleisli}
import cats.implicits._
import cats.{Applicative, MonadError}
import io.taig.schelm.algebra.{Dom, Patcher, Renderer}
import io.taig.schelm.css.data.{CssDiff, CssHtmlDiff, Selector, Style, StylesheetHtmlAttachedReference}
import io.taig.schelm.data.{Html, HtmlAttachedReference, HtmlDiff}

object CssHtmlPatcher {
  def apply[F[_]: Applicative](
      html: Patcher[F, HtmlAttachedReference[F], HtmlDiff[F]],
      css: Patcher[F, Map[Selector, Style], List[CssDiff]]
  ): Patcher[F, StylesheetHtmlAttachedReference[F], CssHtmlDiff[F]] = Kleisli {
    case (StylesheetHtmlAttachedReference(stylesheet, reference), diff) =>
      diff.value match {
        case Ior.Left(diff)              => html.run(reference, diff).map(StylesheetHtmlAttachedReference(stylesheet, _))
        case Ior.Right(diff)             => ??? // css.run((stylesheet, diff.toList)).map(???)
        case Ior.Both(htmlDiff, cssDiff) => ??? // html.patch(nodes, htmlDiff) *> css.patch(styles, cssDiff.toList)
      }
  }

  def default[F[_]: MonadError[*[_], Throwable]](
      dom: Dom[F],
      renderer: Renderer[F, Html[F], List[Dom.Node]]
  ): Patcher[F, (List[Dom.Node], Map[Selector, Style]), CssHtmlDiff[F]] =
    ??? // CssHtmlPatcher(HtmlPatcher(dom, renderer), Patcher.noop)
}

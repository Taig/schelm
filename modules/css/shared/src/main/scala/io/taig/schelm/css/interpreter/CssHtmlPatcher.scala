package io.taig.schelm.css.interpreter

import cats.data.Ior
import cats.implicits._
import cats.{Applicative, MonadError}
import io.taig.schelm.algebra.{Dom, Renderer}
import io.taig.schelm.css.data.{CssDiff, CssHtmlDiff, Selector, Style}
import io.taig.schelm.data.{Html, HtmlDiff, Patcher}
import io.taig.schelm.interpreter.HtmlPatcher

final class CssHtmlPatcher[F[_]: Applicative, Event](
    html: Patcher[F, List[Dom.Node], HtmlDiff[Event]],
    css: Patcher[F, Map[Selector, Style], List[CssDiff]]
) extends Patcher[F, (List[Dom.Node], Map[Selector, Style]), CssHtmlDiff[Event]] {
  override def patch(structure: (List[Dom.Node], Map[Selector, Style]), diff: CssHtmlDiff[Event]): F[Unit] = {
    val (nodes, styles) = structure

    diff.value match {
      case Ior.Left(diff)              => html.patch(nodes, diff)
      case Ior.Right(cssDiff)          => css.patch(styles, cssDiff.toList)
      case Ior.Both(htmlDiff, cssDiff) => html.patch(nodes, htmlDiff) *> css.patch(styles, cssDiff.toList)
    }
  }
}

object CssHtmlPatcher {
  def apply[F[_]: Applicative, Event](
      html: Patcher[F, List[Dom.Node], HtmlDiff[Event]],
      css: Patcher[F, Map[Selector, Style], List[CssDiff]]
  ): Patcher[F, (List[Dom.Node], Map[Selector, Style]), CssHtmlDiff[Event]] =
    new CssHtmlPatcher[F, Event](html, css)

  def default[F[_]: MonadError[*[_], Throwable], Event](
      dom: Dom[F, Event],
      renderer: Renderer[F, Html[Event], List[Dom.Node]]
  ): Patcher[F, (List[Dom.Node], Map[Selector, Style]), CssHtmlDiff[Event]] =
    CssHtmlPatcher(HtmlPatcher(dom, renderer), Patcher.noop)
}

package io.taig.schelm.css.interpreter

import cats.Parallel
import cats.effect.Concurrent
import io.taig.schelm.algebra.{Dom, Schelm}
import io.taig.schelm.css.data.CssHtml

object CssHtmlSchelm {
  def default[F[_]: Concurrent: Parallel, View, Structure, Diff](dom: Dom[F])(
      main: Dom.Element
  ): F[Schelm[F, CssHtml[F]]] = ???
//    CssHtmlAttacher.default[F, Event](dom)(main).map { attacher =>
//      val renderer = HtmlRenderer[F, Event](dom)
//      val differ = CssHtmlDiffer.default[Event]
//      val patcher = Patcher.noop[F, (HtmlReference[Event], Map[Selector, Style]), CssHtmlDiff[Event]] // CssHtmlPatcher.default(dom, renderer)
//      DomSchelm(manager, CssHtmlRenderer(renderer), attacher, differ, patcher)
//    }
}

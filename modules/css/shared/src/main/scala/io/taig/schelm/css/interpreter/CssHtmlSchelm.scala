package io.taig.schelm.css.interpreter

import cats.Parallel
import cats.effect.Concurrent
import cats.implicits._
import io.taig.schelm.algebra.{Dom, EventManager, Schelm}
import io.taig.schelm.css.data.{CssHtml, CssHtmlDiff, Selector, Style}
import io.taig.schelm.data.{HtmlReference, Patcher}
import io.taig.schelm.interpreter.{DomSchelm, HtmlRenderer}

object CssHtmlSchelm {
  def default[F[_]: Concurrent: Parallel, View, Event, Structure, Diff](dom: Dom[F])(
      main: dom.Element,
      manager: EventManager[F, Event]
  ): F[Schelm[F, CssHtml[Event], Event]] =
    CssHtmlAttacher.default[F, Event](dom)(main).map { attacher =>
      val renderer = HtmlRenderer[F, Event](dom)
      val differ = CssHtmlDiffer.default[Event]
      val patcher =
        Patcher.noop[F, (HtmlReference[Event, dom.Node, dom.Element, dom.Text], Map[Selector, Style]), CssHtmlDiff[
          Event
        ]] // CssHtmlPatcher.default(dom, renderer)
      DomSchelm(manager, CssHtmlRenderer(renderer), attacher, differ, patcher)
    }
}

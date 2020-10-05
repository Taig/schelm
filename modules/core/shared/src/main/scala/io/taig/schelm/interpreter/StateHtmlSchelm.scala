package io.taig.schelm.interpreter

import cats.arrow.FunctionK
import cats.data.Kleisli
import cats.effect.Concurrent
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Renderer, Schelm}
import io.taig.schelm.data.{Html, HtmlReference, Path, StateHtml}

object StateHtmlSchelm {
  def default[F[_]: Concurrent](dom: Dom[F])(root: Dom.Element): F[Schelm[F, StateHtml[F]]] = {
    QueueStateManager.empty[F, Html[F]].map { states =>
      val renderer: Renderer[F, StateHtml[F], HtmlReference[F]] = StateHtmlRenderer[F](states)
        .mapK(λ[FunctionK[Kleisli[F, Path, *], F]](_.run(Path.Empty)))
        .andThen(HtmlRenderer[F](dom))

      val attacher = HtmlReferenceAttacher.default(dom)(root)
      val differ = HtmlDiffer[F]
      val patcher = HtmlPatcher(dom, HtmlRenderer[F](dom))

      // DomSchelm(states, renderer, attacher, differ, patcher)(modify = ???)
      ???
    }
  }
}

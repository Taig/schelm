package io.taig.schelm.interpreter

import cats.data.Kleisli
import cats.effect.MonadCancel
import io.taig.schelm.algebra.{Attacher, Dom}
import io.taig.schelm.data._

object HtmlReferenceAttacher {
  def apply[F[_]: MonadCancel[*[_], Throwable]](
      attacher: Attacher[F, Vector[Dom.Node], Dom.Element]
  ): Attacher[F, HtmlReference[F], Dom.Element] = Kleisli(html => attacher.run(html.dom))

  def default[F[_]: MonadCancel[*[_], Throwable]](
      dom: Dom[F]
  )(root: Dom.Element): Attacher[F, HtmlReference[F], Dom.Element] =
    HtmlReferenceAttacher(NodeAttacher(dom)(root))
}

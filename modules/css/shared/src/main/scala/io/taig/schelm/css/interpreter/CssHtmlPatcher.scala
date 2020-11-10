package io.taig.schelm.css.interpreter

import cats.data.{Ior, Kleisli}
import cats.implicits._
import cats.{Applicative, MonadError}
import io.taig.schelm.algebra.{Dom, Patcher}
import io.taig.schelm.css.data._
import io.taig.schelm.data.{HtmlDiff, HtmlHydratedReference}
import io.taig.schelm.interpreter.HtmlPatcher

object CssHtmlPatcher {
  def apply[F[_]: Applicative](
      html: Patcher[F, HtmlHydratedReference[F], HtmlDiff[F]],
      css: Patcher[F, Map[Selector, Style], List[CssDiff]]
  ): Patcher[F, StyledHtmlHydratedReference[F], CssHtmlDiff[F]] = Kleisli {
    case (x @ StyledHtmlHydratedReference(styles, reference), diff) =>
      diff.value match {
        case Ior.Left(diff)  => html.run((reference, diff)).map(StyledHtmlHydratedReference(styles, _))
        case Ior.Right(diff) => css.run((styles, diff.toList)).as(x)
        case Ior.Both(htmlDiff, cssDiff) =>
          css
            .run((styles, cssDiff.toList)) *> html.run((reference, htmlDiff)).map(reference => x.copy(html = reference))
      }
  }

  def default[F[_]: MonadError[*[_], Throwable]](
      dom: Dom[F]
  ): Patcher[F, StyledHtmlHydratedReference[F], CssHtmlDiff[F]] =
    CssHtmlPatcher(HtmlPatcher.default(dom), Patcher.noop)
}

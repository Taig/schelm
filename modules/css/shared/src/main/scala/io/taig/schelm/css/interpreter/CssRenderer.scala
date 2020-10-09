package io.taig.schelm.css.interpreter

import cats.data.Kleisli
import cats.implicits._
import cats.{Applicative, Id}
import io.taig.schelm.algebra.Renderer
import io.taig.schelm.css.data.{Selector, Selectors, Style, Stylesheet}
import io.taig.schelm.util.FunctionKs

object CssRenderer {
  val pure: Map[Selector, Style] => Stylesheet = _.foldLeft(Stylesheet.Empty) {
    case (stylesheet, (selector, style)) =>
      stylesheet |+| Stylesheet.from(style.toRules(Selectors.of(selector)))
  }

  val id: Renderer[Id, Map[Selector, Style], Stylesheet] = Kleisli[Id, Map[Selector, Style], Stylesheet](pure)

  def apply[F[_]: Applicative]: Renderer[F, Map[Selector, Style], Stylesheet] = id.mapK(FunctionKs.liftId[F])
}

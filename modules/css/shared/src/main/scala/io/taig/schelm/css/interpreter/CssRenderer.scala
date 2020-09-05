package io.taig.schelm.css.interpreter

import cats.Id
import cats.implicits._
import io.taig.schelm.algebra.Renderer
import io.taig.schelm.css.data.{Selector, Selectors, Style, Stylesheet}

object CssRenderer extends Renderer[Id, Map[Selector, Style], Stylesheet] {
  override def render(styles: Map[Selector, Style]): Stylesheet =
    styles.foldLeft(Stylesheet.Empty) {
      case (stylesheet, (selector, style)) =>
        stylesheet |+| Stylesheet.from(style.toRules(Selectors.of(selector)))
    }
}

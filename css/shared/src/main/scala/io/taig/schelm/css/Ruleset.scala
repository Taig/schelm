package io.taig.schelm.css

import cats.implicits._
import io.taig.schelm.internal.TextHelpers

final case class Ruleset(selectors: Selectors, declarations: Declarations) {
  override def toString: String = s"$selectors $declarations"
}

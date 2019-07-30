package com.ayendo.schelm.css

import cats.implicits._
import com.ayendo.schelm.internal.TextHelpers

final case class Ruleset(selectors: Selectors, declarations: Declarations) {
  override def toString: String = s"$selectors $declarations"
}

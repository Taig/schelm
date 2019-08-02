package io.taig.schelm.css

final case class Ruleset(selectors: Selectors, declarations: Declarations) {
  override def toString: String = s"$selectors $declarations"
}

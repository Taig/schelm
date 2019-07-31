package io.taig.schelm.css

import cats.implicits._
import io.taig.schelm.internal.TextHelpers

final case class PseudoDeclaration(
    modifier: Modifier,
    declarations: Declarations
) {
  def toRule(selectors: Selectors): Rule =
    Rule.Style(selectors + modifier, declarations)
}

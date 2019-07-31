package io.taig.schelm.css

final case class PseudoDeclaration(
    modifier: Modifier,
    declarations: Declarations
) {
  def toRule(selectors: Selectors): Rule =
    Rule.Style(selectors + modifier, declarations)
}

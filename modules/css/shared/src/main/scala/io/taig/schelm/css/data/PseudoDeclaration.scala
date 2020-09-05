package io.taig.schelm.css.data

final case class PseudoDeclaration(modifier: Modifier, declarations: Declarations) {
  def toRule(selectors: Selectors): Rule.Block = Rule.Block(selectors + modifier, declarations)
}

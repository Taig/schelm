package io.taig.schelm.css.data

final case class PseudoDeclaration(modifier: Modifier, declarations: Declarations) {
  def concat(declarations: Declarations, divider: String): PseudoDeclaration =
    PseudoDeclaration(modifier, this.declarations.concat(declarations, divider))

  def toRule(selectors: Selectors): Rule.Block = Rule.Block(selectors + modifier, declarations)
}

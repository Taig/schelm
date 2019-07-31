package io.taig.schelm.dsl

import io.taig.schelm.css.{
  Declaration,
  Declarations,
  Modifier,
  PseudoDeclaration
}

trait CssPseudoDsl {
  def pseudo(
      modifier: String
  )(declarations: Declaration*): PseudoDeclaration =
    PseudoDeclaration(Modifier(modifier), Declarations.from(declarations))

  def after(declarations: Declaration*): PseudoDeclaration =
    pseudo("::after")(declarations: _*)

  def before(declarations: Declaration*): PseudoDeclaration =
    pseudo("::before")(declarations: _*)
}

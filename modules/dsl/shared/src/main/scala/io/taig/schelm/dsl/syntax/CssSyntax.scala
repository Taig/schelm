package io.taig.schelm.dsl.syntax

import io.taig.schelm.css.data.{CssWidget, Declaration, Declarations, PseudoDeclarations, Style}
import io.taig.schelm.dsl.data.Tagged.@@
import io.taig.schelm.dsl.operation.CssOperation

trait CssSyntax[Event, Context, Tag] {
  final def style(declarations: Declaration*): CssWidget[Event, Context] @@ Tag =
    css.set(Style(Declarations.from(declarations), PseudoDeclarations.Empty))

  def css: CssOperation[Event, Context, Tag]
}

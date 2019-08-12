package io.taig.schelm

import io.taig.schelm.css._

package object dsl {
  type DeclarationOrPseudo = Either[Declaration, PseudoDeclaration]

  val widget: StyledHtmlDsl = StyledHtmlDsl
}

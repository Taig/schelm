package io.taig.schelm.dsl.syntax

import io.taig.schelm.css.data.{Declaration, Declarations, Modifier, PseudoDeclaration, Style}
import io.taig.schelm.dsl.data.DslDeclaration
import io.taig.schelm.dsl.util.DeclarationValue

trait style {
  implicit class DeclarationNameOps(name: Declaration.Name) {
    def :=[A](value: A)(implicit evidence: DeclarationValue[A]): DslDeclaration =
      DslDeclaration(name, evidence.lift(value))
  }

  implicit class StyleOps(style: Style) {
    def &(modifier: String)(values: DslDeclaration*): Style =
      style.copy(pseudos = style.pseudos :+ PseudoDeclaration(Modifier(modifier), Declarations.from(values.collect {
        case DslDeclaration(key, Some(value)) => Declaration(key, value)
      })))
  }

  def css(values: DslDeclaration*): Style =
    Style.from(values.collect { case DslDeclaration(key, Some(value)) => Declaration(key, value) })

  val background: Declaration.Name = Declaration.Name("background")
  val backgroundColor: Declaration.Name = Declaration.Name("background-color")
  val border: Declaration.Name = Declaration.Name("border")
  val borderRadius: Declaration.Name = Declaration.Name("border-radius")
  val boxShadow: Declaration.Name = Declaration.Name("box-shadow")
  val color: Declaration.Name = Declaration.Name("color")
  val cursor: Declaration.Name = Declaration.Name("cursor")
  val fontFamily: Declaration.Name = Declaration.Name("font-family")
  val fontSize: Declaration.Name = Declaration.Name("font-size")
  val fontWeight: Declaration.Name = Declaration.Name("font-weight")
  val letterSpacing: Declaration.Name = Declaration.Name("letter-spacing")
  val margin: Declaration.Name = Declaration.Name("margin")
  val outline: Declaration.Name = Declaration.Name("outline")
  val padding: Declaration.Name = Declaration.Name("padding")
  val textTransform: Declaration.Name = Declaration.Name("text-transform")
  val transition: Declaration.Name = Declaration.Name("transition")
}

object style extends style

package io.taig.schelm.dsl

import cats.syntax.all._
import io.taig.schelm.css.data.{Declaration, Declarations, Modifier, PseudoDeclarations, Style}

trait styles {
  implicit final class DeclarationNameOps(name: Declaration.Name) {
    def :=[A](value: A)(implicit evidence: OptionalDeclaration.Evidence[A]): OptionalDeclaration =
      OptionalDeclaration(name, evidence.lift(value))
  }

  def vendorPrefix(vendor: String, name: String): Declaration.Name = Declaration.Name(s"-$vendor-$name")

  def webkit(name: String): Declaration.Name = vendorPrefix("webkit", name)
  def moz(name: String): Declaration.Name = vendorPrefix("moz", name)
  def opera(name: String): Declaration.Name = vendorPrefix("o", name)
  def edge(name: String): Declaration.Name = vendorPrefix("ms", name)

  val zero: Declaration.Value = Declaration.Value("0")
  val none: Declaration.Value = Declaration.Value("none")

  val borderBox: Declaration.Value = Declaration.Value("border-box")
  val block: Declaration.Value = Declaration.Value("block")
  val inlineBlock: Declaration.Value = Declaration.Value("inline-block")
  val flex: Declaration.Value = Declaration.Value("flex")
  val inlineFlex: Declaration.Value = Declaration.Value("inline-flex")
  val pointer: Declaration.Value = Declaration.Value("pointer")

  val active: Modifier = Modifier(":active")
  val focus: Modifier = Modifier(":focus")
  val hover: Modifier = Modifier(":hover")

  val background: Declaration.Name = Declaration.Name("background")
  val backgroundColor: Declaration.Name = Declaration.Name("background-color")
  val border: Declaration.Name = Declaration.Name("border")
  val borderColor: Declaration.Name = Declaration.Name("border-color")
  val borderRadius: Declaration.Name = Declaration.Name("border-radius")
  val borderWidth: Declaration.Name = Declaration.Name("border-width")
  val boxShadow: Declaration.Name = Declaration.Name("box-shadow")
  val boxSizing: Declaration.Name = Declaration.Name("box-sizing")
  val color: Declaration.Name = Declaration.Name("color")
  val cursor: Declaration.Name = Declaration.Name("cursor")
  val display: Declaration.Name = Declaration.Name("display")
  val fontFamily: Declaration.Name = Declaration.Name("font-family")
  val fontSize: Declaration.Name = Declaration.Name("font-size")
  val fontWeight: Declaration.Name = Declaration.Name("font-weight")
  val lineHeight: Declaration.Name = Declaration.Name("line-height")
  val letterSpacing: Declaration.Name = Declaration.Name("letter-spacing")
  val margin: Declaration.Name = Declaration.Name("margin")
  val marginLeft: Declaration.Name = Declaration.Name("margin-left")
  val marginRight: Declaration.Name = Declaration.Name("margin-right")
  val marginTop: Declaration.Name = Declaration.Name("margin-top")
  val marginBottom: Declaration.Name = Declaration.Name("margin-bottom")
  val outline: Declaration.Name = Declaration.Name("outline")
  val padding: Declaration.Name = Declaration.Name("padding")
  val position: Declaration.Name = Declaration.Name("position")
  val textTransform: Declaration.Name = Declaration.Name("text-transform")
  val transition: Declaration.Name = Declaration.Name("transition")
  val width: Declaration.Name = Declaration.Name("width")
}

object styles extends styles {
  def apply(declarations: OptionalDeclaration*): Style =
    Style(Declarations.from(declarations.mapFilter(_.toDeclaration)), PseudoDeclarations.Empty)
}

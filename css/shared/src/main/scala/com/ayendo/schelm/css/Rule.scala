package com.ayendo.schelm.css

import com.ayendo.schelm.internal.TextHelpers

sealed abstract class Rule extends Product with Serializable {
  override def toString: String = this match {
    case Rule.Charset(value)   => s"@charset $value"
    case Rule.Import(value)    => s"@import $value"
    case Rule.Namespace(value) => s"@namespace $value"
    case Rule.Style(selectors, declarations) =>
      s"""$selectors {
         |${TextHelpers.ident(2, declarations.toString)}
         |}""".stripMargin
    case Rule.Page(selectors, declarations) =>
      s"""@page $selectors {
         |${TextHelpers.ident(2, declarations.toString)}
         |}""".stripMargin
    case Rule.FontFace(declarations) =>
      s"""@font-face {
         |${TextHelpers.ident(2, declarations.toString)}
         |}""".stripMargin
    case Rule.Media(selector, declarations) =>
      s"""@media $selector {
         |${TextHelpers.ident(2, declarations.toString)}
         |}""".stripMargin
    case Rule.Keyframes(identifier, declarations) =>
      s"""@keyframes $identifier {
         |${TextHelpers.ident(2, declarations.toString)}
         |}""".stripMargin
    case Rule.Viewport(declarations) =>
      s"""@viewport {
         |${TextHelpers.ident(2, declarations.toString)}
         |}""".stripMargin
    case Rule.Support(selector, stylesheet) =>
      s"""@support $selector {
         |${TextHelpers.ident(2, stylesheet.toString)}
         |}""".stripMargin
    case Rule.Raw(value) => value
  }
}

object Rule {
  final case class Charset(value: String) extends Rule
  final case class Import(value: String) extends Rule
  final case class Namespace(value: String) extends Rule
  final case class Style(selectors: Selectors, declarations: Declarations)
      extends Rule
  final case class Page(selectors: Selectors, declarations: Declarations)
      extends Rule
  final case class FontFace(declarations: Declarations) extends Rule
  final case class Media(selector: String, declarations: Declarations)
      extends Rule
  final case class Keyframes(identifier: String, declarations: Declarations)
      extends Rule
  final case class Viewport(declarations: Declarations) extends Rule
  final case class Support(selector: String, stylesheet: Stylesheet)
      extends Rule
  final case class Raw(value: String) extends Rule
}

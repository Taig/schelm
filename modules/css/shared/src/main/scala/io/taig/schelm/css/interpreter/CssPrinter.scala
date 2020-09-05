package io.taig.schelm.css.interpreter

import io.taig.schelm.algebra.Printer
import io.taig.schelm.css.data.Rule.{Block, Context, Directive}
import io.taig.schelm.css.data.{Declaration, Declarations, Rule, Selectors, Stylesheet}

object CssPrinter extends Printer[Stylesheet] { self =>
  override def print(stylesheet: Stylesheet, pretty: Boolean): String = {
    val rules = stylesheet.values.map(rule(pretty))
    if (pretty) rules.mkString("\n\n") else rules.mkString(" ")
  }

  def rule(pretty: Boolean): Rule => String = {
    case Context(value, stylesheet) if pretty =>
      s"""$value {
         |  ${print(stylesheet, pretty)}
         |}""".stripMargin
    case Context(value, stylesheet) => s"$value{${print(stylesheet, pretty)}}"
    case Directive(value)           => s"$value"
    case Block(selectors, declarations) if pretty =>
      s"""${self.selectors(pretty)(selectors)} {
         |  ${self.declarations(pretty)(declarations)}
         |}""".stripMargin
    case Block(selectors, declarations) =>
      s"${self.selectors(pretty)(selectors)}{${self.declarations(pretty)(declarations)}}"
  }

  def selectors(pretty: Boolean): Selectors => String =
    selectors => selectors.values.toList.map(_.value).mkString(if (pretty) ", " else ",")

  def declarations(pretty: Boolean): Declarations => String = { declarations =>
    val rows = declarations.values.map(declaration(pretty))
    if (pretty) rows.mkString("", ";\n", ";") else rows.mkString(";")
  }

  def declaration(pretty: Boolean): Declaration => String = {
    case Declaration(Declaration.Name(name), Declaration.Value(value)) if pretty => s"$name: $value"
    case Declaration(Declaration.Name(name), Declaration.Value(value))           => s"$name:$value"
  }
}

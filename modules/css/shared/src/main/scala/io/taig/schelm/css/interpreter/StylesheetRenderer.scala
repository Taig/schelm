package io.taig.schelm.css.interpreter

import cats.arrow.FunctionK
import cats.{Applicative, Id => Identity}
import io.taig.schelm.algebra.Renderer
import io.taig.schelm.css.data._
import io.taig.schelm.util.Text

final class StylesheetRenderer(pretty: Boolean) extends Renderer[Identity, Stylesheet, String] {
  override def render(stylesheet: Stylesheet): String = rules(stylesheet.values)

  val rules: List[Rule] => String = _.map(rule).mkString(if (pretty) "\n\n" else " ")

  val rule: Rule => String = {
    case Rule.Context(value, stylesheet) if pretty =>
      s"""$value {
         |  ${Text.align(2)(render(stylesheet))}
         |}""".stripMargin
    case Rule.Context(value, stylesheet) => s"$value{${render(stylesheet)}}"
    case Rule.Directive(value)           => s"$value"
    case Rule.Block(selectors, declarations) if pretty =>
      s"""${this.selectors(selectors)} {
         |  ${Text.align(2)(this.declarations(declarations))}
         |}""".stripMargin
    case Rule.Block(selectors, declarations) =>
      s"${this.selectors(selectors)}{${this.declarations(declarations)}}"
  }

  val selectors: Selectors => String =
    selectors => selectors.values.toList.map(_.value).mkString(if (pretty) ", " else ",")

  val declarations: Declarations => String = { declarations =>
    val rows = declarations.values.map(declaration)
    if (pretty) rows.mkString("", ";\n", ";") else rows.mkString(";")
  }

  val declaration: Declaration => String = { case Declaration(Declaration.Name(name), Declaration.Value(value)) =>
    if (pretty) s"$name: $value" else s"$name:$value"
  }
}

object StylesheetRenderer {
  def identity(pretty: Boolean): Renderer[Identity, Stylesheet, String] = new StylesheetRenderer(pretty)

  def apply[F[_]](pretty: Boolean)(implicit F: Applicative[F]): Renderer[F, Stylesheet, String] =
    identity(pretty).mapK(FunctionK.liftFunction[Identity, F](F.pure))
}

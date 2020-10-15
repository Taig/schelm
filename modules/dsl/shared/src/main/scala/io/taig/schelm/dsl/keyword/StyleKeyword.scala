//package io.taig.schelm.dsl.keyword
//
//import io.taig.schelm.css.data.{Declaration, Style}
//import io.taig.schelm.dsl.data.DslDeclaration
//
//trait StyleKeyword {
//  def styles(declarations: DslDeclaration*): Style =
//    Style.from(declarations.collect { case DslDeclaration(name, Some(value)) => Declaration(name, value) })
//
//  val backgroundColor: Declaration.Name = Declaration.Name("background-color")
//  val boxShadow: Declaration.Name = Declaration.Name("box-shadow")
//  val color: Declaration.Name = Declaration.Name("color")
//  val fontFamily: Declaration.Name = Declaration.Name("font-family")
//  val fontSize: Declaration.Name = Declaration.Name("font-size")
//  val fontWeight: Declaration.Name = Declaration.Name("font-weight")
//  val letterSpacing: Declaration.Name = Declaration.Name("letter-spacing")
//  val lineHeight: Declaration.Name = Declaration.Name("line-height")
//  val margin: Declaration.Name = Declaration.Name("margin")
//  val maxWidth: Declaration.Name = Declaration.Name("max-width")
//  val padding: Declaration.Name = Declaration.Name("padding")
//  val textDecoration: Declaration.Name = Declaration.Name("text-decoration")
//  val textTransform: Declaration.Name = Declaration.Name("text-transform")
//}

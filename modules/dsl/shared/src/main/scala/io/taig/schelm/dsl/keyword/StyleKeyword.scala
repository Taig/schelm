package io.taig.schelm.dsl.keyword

import io.taig.schelm.css.data.Declaration

trait StyleKeyword {
  val backgroundColor: Declaration.Name = Declaration.Name("background-color")

  val boxShadow: Declaration.Name = Declaration.Name("box-shadow")

  val color: Declaration.Name = Declaration.Name("color")

  val fontFamily: Declaration.Name = Declaration.Name("font-family")

  val lineHeight: Declaration.Name = Declaration.Name("line-height")

  val margin: Declaration.Name = Declaration.Name("margin")

  val maxWidth: Declaration.Name = Declaration.Name("max-width")

  val padding: Declaration.Name = Declaration.Name("padding")
}

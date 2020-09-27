package io.taig.schelm.dsl

import io.taig.schelm.css.data.Declaration

trait StylesheetDsl {
  val backgroundColor: Declaration.Name = Declaration.Name("background-color")

  val boxShadow: Declaration.Name = Declaration.Name("box-shadow")

  val color: Declaration.Name = Declaration.Name("color")

  val fontFamily: Declaration.Name = Declaration.Name("font-family")
}

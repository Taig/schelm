package io.taig.schelm.dsl

import io.taig.schelm.css.data.Stylesheet

trait StylesheetDsl {
  val color: Stylesheet.Rule.Name = Stylesheet.Rule.Name("color")
}

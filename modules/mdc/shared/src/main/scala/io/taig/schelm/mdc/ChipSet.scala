package io.taig.schelm.mdc

import io.taig.schelm.dsl.DslWidget.Element
import io.taig.schelm.dsl._

object ChipSet {
  def apply(): Element.Normal[Any] =
    div.attrs(cls := "mdc-chip-set", role := "grid")
}

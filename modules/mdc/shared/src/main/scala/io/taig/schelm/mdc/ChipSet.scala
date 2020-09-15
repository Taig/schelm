package io.taig.schelm.mdc

import io.taig.schelm.data.{Attributes, Children}
import io.taig.schelm.dsl._
import io.taig.schelm.dsl.data.DslWidget

final case class ChipSet(chips: Children[Chip]) extends DslWidget.Component[MdcEvent[Nothing], Any] {
  override val render: DslWidget[MdcEvent[Nothing], Any] =
    div(attributes = Attributes.of(a.cls := "mdc-chip-set", a.role := "grid"), children = chips)
}

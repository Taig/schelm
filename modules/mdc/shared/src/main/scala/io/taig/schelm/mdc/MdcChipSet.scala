package io.taig.schelm.mdc

import io.taig.schelm.data.{Attributes, Children}
import io.taig.schelm.dsl._
import io.taig.schelm.dsl.data.DslWidget

final case class MdcChipSet(chips: Children[MdcChip]) extends DslWidget.Component[Nothing, Any] {
  override val render: DslWidget[Nothing, Any] =
    div(attributes = Attributes.of(a.cls := "mdc-chip-set", a.role := "grid"), children = chips)
}

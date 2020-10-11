package io.taig.schelm.mdc

import io.taig.schelm.data.{Attributes, Children}
import io.taig.schelm.dsl._
import io.taig.schelm.dsl.data.DslWidget

final case class MdcChipSet[F[_]](chips: Children[MdcChip[F]]) extends DslWidget.Component[F, Nothing, Any] {
  override val render: DslWidget[F, Nothing, Any] =
    div(attributes = Attributes.of(a.cls := "mdc-chip-set", a.role := "grid"), children = chips)
}

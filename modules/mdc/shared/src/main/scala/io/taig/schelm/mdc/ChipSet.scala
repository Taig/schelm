package io.taig.schelm.mdc

import io.taig.schelm.data.{Attributes, Children}
import io.taig.schelm.dsl._
import io.taig.schelm.dsl.data.DslWidget

object ChipSet {
  def apply[A](children: Children[DslWidget[A]]): DslWidget.Element.Normal[A] =
    div(attributes = Attributes.of(a.cls := "mdc-chip-set", a.role := "grid"), children = children)
}

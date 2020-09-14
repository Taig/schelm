package io.taig.schelm.mdc

import cats.implicits._
import io.taig.schelm.dsl._
import io.taig.schelm.mdc.Mdc

object Chip {
  def apply(
      label: String,
      selected: Boolean = false,
           index: Int = 0
  ): DslWidget.Element.Normal[Any] = {
    val classes = List("mdc-chip") ++ selected.guard[List].as("mdc-chip--selected")

    div.attrs(cls := classes, role := "row").mounted(Mdc.chip)(
      div.attrs(cls := "mdc-chip__ripple"),
      span.attrs(role := "gridcell")(
        span.attrs(cls := "mdc-chip__primary-action", role := "button", tabindex := String.valueOf(index))(
          span.attrs(cls := "mdc-chip__text")(text(label))
        )
      )
    )
  }
}

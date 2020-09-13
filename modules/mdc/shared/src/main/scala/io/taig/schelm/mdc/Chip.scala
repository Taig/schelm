package io.taig.schelm.mdc

import cats.implicits._
import io.taig.schelm.dsl._

object Chip {
  def apply(
      label: String,
      selected: Boolean = false
  ): DslWidget.Element.Normal[Nothing, Any] = {
    val classes = List("mdc-chip") ++ selected.guard[List].as("mdc-chip--selected")

    div.attrs(cls := classes, role := "row")
//    (
//      div.attrs(cls := "mdc-chip__ripple"),
//      span.attrs(role := "gridcell")(
//        span.attrs(role := "button", tabindex := "0", cls := "mdc-chip__primary-action")(
//          span.attrs(cls := "mdc-chip__text")(text(label))
//        )
//      )
//    )
  }
}

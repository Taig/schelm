package io.taig.schelm.mdc

import cats.implicits._
import io.taig.schelm.css.data.CssWidget
import io.taig.schelm.dsl._
import io.taig.schelm.dsl.internal.Has
import io.taig.schelm.dsl.internal.Tagged.@@

object Chip {
  def apply(
      label: String,
      selected: Boolean = false
  ): CssWidget[Nothing, Any] @@ Has.Attributes with Has.Listeners with Has.Css with Has.Children = {
    val classes = List("mdc-chip") ++ selected.guard[List].as("mdc-chip--selected")

    div.attrs(cls := classes, role := "row")(
      div.attrs(cls := "mdc-chip__ripple"),
      span.attrs(role := "gridcell")(
        span.attrs(role := "button", tabindex := "0", cls := "mdc-chip__primary-action")(
          span.attrs(cls := "mdc-chip__text")(text(label))
        )
      )
    )
  }
}

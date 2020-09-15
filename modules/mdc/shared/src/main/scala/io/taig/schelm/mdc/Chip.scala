package io.taig.schelm.mdc

import cats.implicits._
import io.taig.schelm.css.data.CssNode
import io.taig.schelm.data.{Attributes, Children, Component, Lifecycle, Widget}
import io.taig.schelm.dsl._
import io.taig.schelm.dsl.data.DslWidget
import io.taig.schelm.mdc.Mdc

final case class Chip(
                  label: String,
                  icon: Option[String] = None,
                  selected: Boolean = false,
                  tabindex: Int = -1
                ) extends DslWidget[Any] {
  override def render: Widget[Any, CssNode[Component[DslWidget[Any]]]] = ???

  def apply(
      label: String,
      icon: Option[String] = None,
      selected: Boolean = false,
      tabindex: Int = -1
  ): DslWidget[Any] = {
    val classes = List("mdc-chip") ++ selected.guard[List].as("mdc-chip--selected")

    val children = Children.of(div(attributes = Attributes.of(a.cls := "mdc-chip__ripple"))) ++
      Children.from(icon.map { icon =>
        i(
          attributes = Attributes.of(a.cls := "material-icons mdc-chip__icon mdc-chip__icon--leading"),
          children = Children.of(text(icon))
        )
      }) ++ Children.of(
      span(
        attributes = Attributes.of(a.role := "gridcell"),
        children = Children.of(
          span(
            attributes = Attributes.of(a.cls := "mdc-chip__primary-action", a.role := "button", a.tabindex := tabindex),
            children = Children.of(
              span(attributes = Attributes.of(a.cls := "mdc-chip__text"), children = Children.of(text(label)))
            )
          )
        )
      )
    )

    div(
      attributes = Attributes.of(a.cls := classes, a.role := "row"),
      lifecycle = Lifecycle(mounted = Mdc.chip.some, unmount = none),
      children = children
    )
  }
}

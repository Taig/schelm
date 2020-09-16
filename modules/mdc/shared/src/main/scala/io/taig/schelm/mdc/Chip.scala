package io.taig.schelm.mdc

import cats.implicits._
import io.taig.schelm.data.{Attributes, Children, Platform}
import io.taig.schelm.dsl._
import io.taig.schelm.dsl.data.DslWidget

final case class Chip(
    label: String,
    icon: Option[(String, Chip.Icon.Position)] = None,
    selected: Boolean = false,
    tabindex: Int = -1
) extends DslWidget.Component[MdcEvent[Nothing], Any] {
  object mounted extends Callback.Element[MdcEvent[Nothing]] {
    override def apply(platform: Platform)(element: platform.Element): Option[MdcEvent[Nothing]] =
      MdcEvent.ComponentMounted(Component.Chip, element).some
  }

  val body: DslWidget[Nothing, Any] = span(
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

  override val render: DslWidget[MdcEvent[Nothing], Any] = div(
    attributes =
      Attributes.of(a.cls := List("mdc-chip") ++ selected.guard[List].as("mdc-chip--selected"), a.role := "row"),
    lifecycle = lifecycle.element(mounted = mounted),
    children = Children.of(div(attributes = Attributes.of(a.cls := "mdc-chip__ripple"))) ++
      Children.from(icon.collect { case (name, position @ Chip.Icon.Position.Leading) => Chip.Icon(name, position) }) ++
      Children.of(body) ++
      Children.from(icon.collect {
        case (name, position @ Chip.Icon.Position.Trailing) =>
          span(
            attributes = Attributes.of(a.role := "gridcell"),
            children = Children.of(Chip.Icon(name, position))
          )
      })
  )
}

object Chip {
  object Icon {
    sealed abstract class Position extends Product with Serializable

    object Position {
      final case object Leading extends Position
      final case object Trailing extends Position
    }

    def apply(name: String, position: Position): DslWidget[Nothing, Any] = {
      val cls = position match {
        case Position.Leading  => "leading"
        case Position.Trailing => "trailing"
      }

      i(
        attributes = Attributes.of(a.cls := s"material-icons mdc-chip__icon mdc-chip__icon--$cls"),
        children = Children.of(text(name))
      )
    }
  }
}

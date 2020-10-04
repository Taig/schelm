package io.taig.schelm.mdc

import cats.effect.Sync
import cats.implicits._
import io.taig.schelm.data.{Attributes, Children}
import io.taig.schelm.dsl._
import io.taig.schelm.dsl.data.DslWidget
import io.taig.schelm.mdc.MdcChip.Prefix
import io.taig.schelm.mdc.internal.MdcLifecycle

final case class MdcChip[F[_]: Sync](
    label: String,
    icon: Option[(String, MdcChip.Icon.Position)] = None,
    selected: Boolean = false,
    tabindex: Int = -1
) extends DslWidget.Component[F, Any] {
  val body: DslWidget[F, Any] = span(
    attributes = Attributes.of(a.role := "gridcell"),
    children = Children.of(
      span(
        attributes = Attributes.of(a.cls := s"${Prefix}__primary-action", a.role := "button", a.tabindex := tabindex),
        children = Children.of(
          span(attributes = Attributes.of(a.cls := s"${Prefix}__text"), children = Children.of(text(label)))
        )
      )
    )
  )

  override val render: DslWidget[F, Any] = div(
    attributes = Attributes.of(a.cls := List(Prefix) ++ selected.guard[List].as(s"$Prefix--selected"), a.role := "row"),
    lifecycle = MdcLifecycle.chip[F],
    children = Children.of(div(attributes = Attributes.of(a.cls := s"${Prefix}__ripple"))) ++
      Children.from(icon.collect {
        case (name, position @ MdcChip.Icon.Position.Leading) => MdcChip.Icon(name, position)
      }) ++
      Children.of(body) ++
      Children.from(icon.collect {
        case (name, position @ MdcChip.Icon.Position.Trailing) =>
          span(
            attributes = Attributes.of(a.role := "gridcell"),
            children = Children.of(MdcChip.Icon(name, position))
          )
      })
  )
}

object MdcChip {
  val Prefix: String = "mdc-chip"

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
        attributes = Attributes.of(a.cls := s"material-icons ${Prefix}__icon ${Prefix}__icon--$cls"),
        children = Children.of(text(name))
      )
    }
  }
}

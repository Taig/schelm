package io.taig.schelm.mdc

import io.taig.schelm.data.{Attributes, Children}
import io.taig.schelm.dsl._
import io.taig.schelm.dsl.data.DslWidget

final case class MdcTopAppBar[+Event, -Context](children: Children[DslWidget[Event, Context]])
    extends DslWidget.Component[Event, Context] {
  override def render: DslWidget[Event, Context] =
    header(attributes = Attributes.of(a.cls := MdcTopAppBar.Prefix), children = children)
}

object MdcTopAppBar {
  val Prefix: String = "mdc-top-app-bar"

  object Section {
    sealed abstract class Alignment extends Product with Serializable

    object Alignment {
      final case object End extends Alignment
      final case object Start extends Alignment
    }

    def apply[Event, Context](
        attributes: Attributes,
        children: Children[DslWidget[Event, Context]]
    ): DslWidget[Event, Context] =
      section(
        attributes = Attributes.of(a.cls := s"${Prefix}__section") ++ attributes,
        children = children
      )

    def aligned[Event, Context](
        alignment: Alignment,
        children: Children[DslWidget[Event, Context]]
    ): DslWidget[Event, Context] = {
      val identifier = alignment match {
        case Alignment.End   => "end"
        case Alignment.Start => "start"
      }

      Section(Attributes.of(a.cls := s"${Prefix}__section--align-$identifier"), children)
    }
  }

  object Title {
    def apply(title: String): DslWidget[Nothing, Any] =
      span(
        attributes = Attributes.of(a.cls := s"${Prefix}__title"),
        children = Children.of(text(title))
      )

    def icon(name: String, label: String): DslWidget[Nothing, Any] =
      MdcIcon(name, label, "button", Attributes.of(a.cls := "mdc-top-app-bar__navigation-icon mdc-icon-button"))
  }

  object Component {
    def row[Event, Context](children: Children[DslWidget[Event, Context]]): DslWidget[Event, Context] =
      div(
        attributes = Attributes.of(a.cls := s"${Prefix}__row"),
        children = children
      )
  }

  def regular(title: String): MdcTopAppBar[Nothing, Any] =
    MdcTopAppBar(
      children = Children.of(
        Component.row(children =
          Children.of(
            Section.aligned(
              Section.Alignment.Start,
              children = Children.of(
                Title.icon(name = "menu", label = "Menu"),
                Title(title)
              )
            )
          )
        )
      )
    )
}

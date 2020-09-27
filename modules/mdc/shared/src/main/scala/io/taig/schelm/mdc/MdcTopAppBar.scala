package io.taig.schelm.mdc

import io.taig.schelm.css.data.Style
import io.taig.schelm.data.{Attributes, Children}
import io.taig.schelm.dsl._
import io.taig.schelm.dsl.data.DslWidget

final case class MdcTopAppBar[+Event](style: Style, children: Children[DslWidget[Event, MdcTheme]])
    extends DslWidget.Component[Event, MdcTheme] {
  override def render: DslWidget[Event, MdcTheme] = contextual { theme =>
    header(
      attributes = Attributes.of(a.cls := MdcTopAppBar.Prefix),
      style = Style.of(backgroundColor := theme.variant.palette.primary) ++ style,
      children = children
    )
  }
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
    def apply(title: String): DslWidget[Nothing, MdcTheme] =
      contextual { theme =>
        span(
          attributes = Attributes.of(a.cls := s"${Prefix}__title"),
          style = Style.of(color := theme.variant.text.primary, fontFamily := theme.fontFamily),
          children = Children.of(text(title))
        )
      }

    def icon(name: String, label: String): DslWidget[Nothing, MdcTheme] = {
      contextual { theme =>
        MdcIcon(
          name,
          label,
          "button",
          Attributes.of(a.cls := "mdc-top-app-bar__navigation-icon mdc-icon-button"),
          style = Style.of(color := s"${theme.variant.text.primary} !important")
        )
      }
    }
  }

  object Component {
    def row[Event, Context](children: Children[DslWidget[Event, Context]]): DslWidget[Event, Context] =
      div(
        attributes = Attributes.of(a.cls := s"${Prefix}__row"),
        children = children
      )
  }

  def regular(title: String, style: Style = Style.Empty): MdcTopAppBar[Nothing] =
    MdcTopAppBar(
      style,
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

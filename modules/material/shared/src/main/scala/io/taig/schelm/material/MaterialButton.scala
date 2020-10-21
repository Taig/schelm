package io.taig.schelm.material

import io.taig.schelm.css.data.Style
import io.taig.schelm.data.{Attributes, Children, Listeners}
import io.taig.schelm.dsl._
import io.taig.schelm.material.MaterialButton.Tag

final case class MaterialButton[+F[_]](
    tag: MaterialButton.Tag,
    label: String,
    theme: MaterialTheme.Button,
    attributes: Attributes,
    style: Style,
    listeners: Listeners[F]
) extends Component[F, Nothing, Any] {
  val styles: Style = css(
    backgroundColor := theme.background,
    border := "none",
    borderRadius := theme.radius,
    color := theme.font.color,
    cursor := "pointer",
    fontFamily := theme.font.family,
    fontSize := theme.font.size,
    fontWeight := theme.font.weight,
    letterSpacing := theme.font.letterSpacing,
    margin := "0",
    outline := "none",
    padding := s"${theme.spacing}px ${theme.spacing * 2}px",
    textTransform := theme.font.transform,
    transition := "background-color 250ms cubic-bezier(0.4, 0, 0.2, 1) 0ms"
  ).&(hover)(backgroundColor := theme.hover.background)
    .&(active)(backgroundColor := theme.active.background) ++
    fontSmoothing ++ style

  override def render: Widget[F, Nothing, Any] = tag match {
    case Tag.A =>
      a(attributes = attributes, style = styles, listeners = listeners, children = Children.of(text(label)))
    case Tag.Button =>
      button(attributes = attributes, style = styles, listeners = listeners, children = Children.of(text(label)))
    case variant: Tag.Input =>
      input(
        attributes = attributes ++ attrs(value := label, tpe := variant.tpe),
        style = styles,
        listeners = listeners
      )
  }
}

object MaterialButton {
  sealed abstract class Tag extends Product with Serializable

  object Tag {
    final case object A extends Tag
    final case object Button extends Tag
    final case class Input(tpe: String) extends Tag
  }

  sealed abstract class Flavor extends Product with Serializable

  object Flavor {
    final case object Primary extends Flavor
    final case object Secondary extends Flavor
    final case object Danger extends Flavor
  }

  def default[F[_]](
      label: String,
      tag: Tag = Tag.Button,
      flavor: Option[Flavor] = None,
      attributes: Attributes = Attributes.Empty,
      style: Style = Style.Empty,
      listeners: Listeners[F] = Listeners.Empty
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    val variant = theme.variant
    val button = flavor match {
      case Some(Flavor.Primary)   => variant.buttons.primary
      case Some(Flavor.Secondary) => variant.buttons.secondary
      case Some(Flavor.Danger)    => variant.buttons.danger
      case None                   => variant.buttons.normal
    }

    MaterialButton[F](tag, label, button, attributes, style, listeners)
  }
}

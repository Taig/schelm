package io.taig.schelm.material

import io.taig.schelm.data.{Children, Listener}
import io.taig.schelm.dsl._
import io.taig.schelm.dsl.data.Property
import org.scalajs.dom.raw.HTMLButtonElement

object MaterialButton {
  sealed abstract class Tag extends Product with Serializable

  object Tag {
    final case object A extends Tag
    final case object Button extends Tag
    final case class Input(tpe: String) extends Tag
  }

  sealed abstract class Variant extends Product with Serializable

  object Variant {
    final case object Primary extends Variant
    final case object Secondary extends Variant
    final case object Danger extends Variant
  }

  def apply[F[_]](
      tag: Tag,
      label: String,
      theme: MaterialTheme.Button,
      onClick: Listener.Action.Target[F, HTMLButtonElement] = effect.noop,
      property: Property[F] = Property.Empty
  ): Widget[F, Nothing, Any] = {
    val style = css(
      backgroundColor := theme.background,
      border := none,
      borderRadius := theme.radius,
      color := theme.font.color,
      cursor := pointer,
      fontFamily := theme.font.family,
      fontSize := theme.font.size,
      fontWeight := theme.font.weight,
      letterSpacing := theme.font.letterSpacing,
      margin := zero,
      outline := none,
      padding := s"${theme.spacing}px ${theme.spacing * 2}px",
      textTransform := theme.font.transform,
      transition := MaterialUtils.transition(backgroundColor)
    ).&(hover)(backgroundColor := theme.hover.background)
      .&(active)(backgroundColor := theme.active.background) ++
      fontSmoothing

    tag match {
      case Tag.A =>
        syntax.html.a(property.prependStyle(style).addListener(click := onClick), children = Children.of(text(label)))
      case Tag.Button =>
        syntax.html
          .button(property.prependStyle(style).addListener(click := onClick), children = Children.of(text(label)))
      case variant: Tag.Input =>
        syntax.html.input(
          property
            .appendAttributes(attrs(value := label, tpe := variant.tpe))
            .prependStyle(style)
            .addListener(click := onClick)
        )
    }
  }

  def themed[F[_]](
      label: String,
      tag: Tag = Tag.Button,
      variant: Option[Variant] = None,
      onClick: Listener.Action.Target[F, HTMLButtonElement] = effect.noop,
      property: Property[F] = Property.Empty
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    val button = variant match {
      case Some(Variant.Primary)   => theme.variant.buttons.primary
      case Some(Variant.Secondary) => theme.variant.buttons.secondary
      case Some(Variant.Danger)    => theme.variant.buttons.danger
      case None                    => theme.variant.buttons.normal
    }

    MaterialButton[F](tag, label, button, onClick, property)
  }
}

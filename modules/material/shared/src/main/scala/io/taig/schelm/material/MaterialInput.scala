package io.taig.schelm.material

import io.taig.schelm.css.data.Style
import io.taig.schelm.data.{Children, Listener}
import io.taig.schelm.dsl._
import io.taig.schelm.dsl.data.Property
import org.scalajs.dom.raw.HTMLInputElement

object MaterialInput {
  final case class Properties[+F[_]](
      root: Property[F] = Property.Empty,
      label: Property[F] = Property.Empty,
      input: Property[F] = Property.Empty,
      helper: Property[F] = Property.Empty
  )

  object Properties {
    val Empty: Properties[Nothing] = Properties()
  }

  sealed abstract class Variant extends Product with Serializable

  object Variant {
    final case object Error extends Variant
    final case object Normal extends Variant
    final case object Success extends Variant
    final case object Warning extends Variant
  }

  object Label {
    def apply[F[_], Event, Context](
        content: String,
        theme: MaterialTheme.Input,
        id: Option[String] = None,
        property: Property[F] = Property.Empty
    ): Widget[F, Nothing, Any] = {
      val style = css(
        display := block,
        margin := s"0 ${theme.spacing * 2}px",
        cursor := (if (id.isDefined) Some(pointer) else None)
      )

      MaterialTypography(
        tag = syntax.html.Label,
        content,
        theme.label,
        property.appendAttributes(attrs(`for` := id)).prependStyle(style)
      )
    }
  }

  object Input {
    def apply[F[_], Event, Context](
        theme: MaterialTheme.Input,
        highlight: Boolean = false,
        id: Option[String] = None,
        value: Option[String] = None,
        placeholder: Option[String] = None,
        reserveHelperSpace: Boolean = false,
        property: Property[F] = Property.Empty
    ): Widget[F, Nothing, Any] = {
      val attributes = attrs(
        syntax.attribute.id := id,
        syntax.attribute.value := value,
        syntax.attribute.placeholder := placeholder
      )

      val borderHover = theme.hover
        .map(color => css().&(hover)(boxShadow := s"inset 0 0 0 2px ${color.toHex}"))
        .getOrElse(Style.Empty)

      val borderFocus =
        if (highlight) css(boxShadow := s"inset 0 0 0 2px ${theme.border.toHex}")
        else
          css(boxShadow := s"inset 0 0 0 1px ${theme.border.toHex}")
            .&(focus)(boxShadow := s"inset 0 0 0 2px ${theme.focus.toHex}")

      val style = css(
        border := none,
        borderRadius := theme.radius,
        boxSizing := borderBox,
        cursor := "text",
        fontFamily := theme.value.family,
        fontSize := theme.value.size,
        margin := 0,
        marginBottom := (if (reserveHelperSpace) Some(theme.helper.lineHeight) else None),
        outline := none,
        padding := theme.spacing * 2,
        transition := MaterialUtils.transition(borderColor),
        width := "100%"
      ) ++ borderHover ++ borderFocus

      syntax.html.input(property.appendAttributes(attributes).prependStyle(style))
    }
  }

  object Helper {
    def apply[F[_], Event, Context](
        content: String,
        theme: MaterialTheme.Input,
        property: Property[F] = Property.Empty
    ): Widget[F, Nothing, Any] = {
      val style = css(
        display := block,
        margin := s"0 ${theme.spacing * 2}px"
      )

      MaterialTypography(tag = syntax.html.P, content, theme.label, property.prependStyle(style))
    }
  }

  def apply[F[_], Event, Context](
      theme: MaterialTheme.Input,
      highlight: Boolean = false,
      label: Option[String] = None,
      id: Option[String] = None,
      value: Option[String] = None,
      placeholder: Option[String] = None,
      helper: Option[String] = None,
      reserveHelperSpace: Boolean = true,
      onChange: Option[Listener.Action[F]] = None,
      onInput: Option[Listener.Action[F]] = None,
      properties: Properties[F] = Properties.Empty
  ): Widget[F, Event, Context] =
    syntax.html.div(
      properties.root,
      Children.fromOption(label.map(Label(_, theme, id, properties.label))) ++
        indexed(
          Input(
            theme,
            highlight,
            id,
            value,
            placeholder,
            reserveHelperSpace && helper.isEmpty,
            properties.input.addListener(syntax.listener.onChange := ???)
          )
        ) ++
        Children.fromOption(helper.map(Helper(_, theme, properties.helper)))
    )

  def themed[F[_]](
      label: Option[String] = None,
      helper: Option[String] = None,
      id: Option[String] = None,
      value: Option[String] = None,
      placeholder: Option[String] = None,
      reserveHelperSpace: Boolean = true,
      variant: Variant = Variant.Normal,
      onChange: Option[Listener.Action[F]] = None,
      onInput: Option[Listener.Action[F]] = None,
      properties: Properties[F] = Properties.Empty
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    val input = variant match {
      case Variant.Error   => theme.variant.inputs.error
      case Variant.Normal  => theme.variant.inputs.normal
      case Variant.Success => theme.variant.inputs.success
      case Variant.Warning => theme.variant.inputs.warning
    }

    MaterialInput(
      input,
      highlight = variant != Variant.Normal,
      label,
      id,
      value,
      placeholder,
      helper,
      reserveHelperSpace,
      onChange,
      onInput,
      properties
    )
  }
}

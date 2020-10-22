package io.taig.schelm.material

import io.taig.schelm.css.data.Style
import io.taig.schelm.data.{Attributes, Children, Lifecycle, Listeners}
import io.taig.schelm.dsl._

final case class MaterialInput[+F[_], +Event, -Context](
    label: Option[Widget[F, Event, Context]],
    input: Widget[F, Event, Context],
    helper: Option[Widget[F, Event, Context]],
    attributes: Attributes = Attributes.Empty,
    listeners: Listeners[F] = Listeners.Empty,
    style: Style = Style.Empty,
    lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
) extends Component[F, Event, Context] {
  override def render: Widget[F, Event, Context] =
    syntax.html.div(
      attributes,
      listeners,
      style,
      lifecycle,
      Children.fromOption(label) ++ indexed(input) ++ Children.fromOption(helper)
    )
}

object MaterialInput {
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
        attributes: Attributes = Attributes.Empty,
        listeners: Listeners[F] = Listeners.Empty,
        style: Style = Style.Empty,
        lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
    ): Widget[F, Nothing, Any] = {
      val pointerFor = if (attributes.contains(`for`)) css(cursor := pointer) else Style.Empty

      val styles = css(
        display := block,
        margin := s"0 ${theme.spacing * 2}px"
      ) ++ pointerFor ++ style

      MaterialTypography(
        tag = syntax.html.Label,
        content,
        theme.label,
        attributes,
        listeners,
        styles,
        lifecycle
      )
    }

    def default[F[_], Event, Context](
        content: String,
        theme: MaterialTheme.Input,
        id: Option[String] = None,
        attributes: Attributes = Attributes.Empty,
        listeners: Listeners[F] = Listeners.Empty,
        style: Style = Style.Empty,
        lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
    ): Widget[F, Nothing, Any] =
      Label(content, theme, attributes ++ attrs(`for` := id), listeners, style, lifecycle)
  }

  object Input {
    def apply[F[_], Event, Context](
        theme: MaterialTheme.Input,
        highlight: Boolean = false,
        attributes: Attributes = Attributes.Empty,
        listeners: Listeners[F] = Listeners.Empty,
        style: Style = Style.Empty,
        lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
    ): Widget[F, Nothing, Any] = {
      val borderHover = theme.hover
        .map(color => css().&(hover)(boxShadow := s"inset 0 0 0 2px ${color.toHex}"))
        .getOrElse(Style.Empty)

      val borderFocus =
        if (highlight) css(boxShadow := s"inset 0 0 0 2px ${theme.border.toHex}")
        else
          css(boxShadow := s"inset 0 0 0 1px ${theme.border.toHex}")
            .&(focus)(boxShadow := s"inset 0 0 0 2px ${theme.focus.toHex}")

      val styles = css(
        border := none,
        borderRadius := theme.radius,
        boxSizing := borderBox,
        cursor := "text",
        fontFamily := theme.value.family,
        fontSize := theme.value.size,
        margin := 0,
        outline := none,
        padding := theme.spacing * 2,
        transition := MaterialUtils.transition(borderColor),
        width := "100%"
      ) ++ borderHover ++ borderFocus ++ style

      syntax.html.input(attributes, listeners, styles, lifecycle)
    }

    def default[F[_], Event, Context](
        theme: MaterialTheme.Input,
        highlight: Boolean = false,
        placeholder: Option[String] = None,
        id: Option[String] = None,
        attributes: Attributes = Attributes.Empty,
        listeners: Listeners[F] = Listeners.Empty,
        style: Style = Style.Empty,
        lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
    ): Widget[F, Nothing, Any] =
      Input(
        theme,
        highlight,
        attributes ++ attrs(syntax.attribute.id := id, syntax.attribute.placeholder := placeholder),
        listeners,
        style,
        lifecycle
      )
  }

  object Helper {
    def apply[F[_], Event, Context](
        content: String,
        theme: MaterialTheme.Input,
        attributes: Attributes = Attributes.Empty,
        listeners: Listeners[F] = Listeners.Empty,
        style: Style = Style.Empty,
        lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
    ): Widget[F, Nothing, Any] = {
      val styles = css(
        display := block,
        margin := s"0 ${theme.spacing * 2}px"
      ) ++ style

      MaterialTypography(
        tag = syntax.html.P,
        content,
        theme.label,
        attributes,
        listeners,
        styles,
        lifecycle
      )
    }
  }

  def auto[F[_]](
      label: Option[String] = None,
      placeholder: Option[String] = None,
      helper: Option[String] = None,
      id: Option[String] = None,
      reserveHelperSpace: Boolean = true,
      variant: Variant = Variant.Normal,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    val input = variant match {
      case Variant.Error   => theme.variant.inputs.error
      case Variant.Normal  => theme.variant.inputs.normal
      case Variant.Success => theme.variant.inputs.success
      case Variant.Warning => theme.variant.inputs.warning
    }

    val reserveHelperSpaceStyle =
      if (reserveHelperSpace && helper.isEmpty)
        css(marginBottom := input.helper.lineHeight)
      else Style.Empty

    MaterialInput(
      label = label.map(Label.default(_, input, id)),
      input =
        Input.default(input, highlight = variant != Variant.Normal, placeholder, id, style = reserveHelperSpaceStyle),
      helper.map(Helper(_, input)),
      attributes,
      listeners,
      style,
      lifecycle
    )
  }
}

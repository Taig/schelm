package io.taig.schelm.material

import io.taig.schelm.css.data.Style
import io.taig.schelm.data.{Attributes, Lifecycle, Listeners}
import io.taig.schelm.dsl._

final case class MaterialInput[+F[_]](
    label: Option[String],
    theme: MaterialTheme.Input,
    attributes: Attributes,
    listeners: Listeners[F],
    styles: MaterialInput.Styles,
    lifecycle: Lifecycle.Element[F]
) extends Component[F, Nothing, Any] {
  val labelStyle: Style = styles.label

  val inputStyle: Style = css(
    border := s"1px solid ${theme.border.toHex}",
    borderRadius := theme.radius,
    cursor := "text",
    fontFamily := theme.font.family,
    fontSize := theme.font.size,
    margin := 1,
    outline := none,
    padding := theme.spacing * 2,
    transition := "border-color 250ms cubic-bezier(0.4, 0, 0.2, 1) 0ms"
  ).&(focus)(
    borderColor := theme.focus,
    borderWidth := 2,
    margin := 0
  ) ++ styles.input

  override def render: Widget[F, Nothing, Any] =
    div(
      children =
//        Children.from(label.map(label => MaterialTypography.default(content = label, tag = Label))) ++
        indexed(input(attributes, listeners, inputStyle, lifecycle))
    )
}

object MaterialInput {
  final case class Styles(label: Style, input: Style)

  def default[F[_]](
      label: Option[String] = None,
      placeholder: Option[String] = None,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialInput(
      label,
      theme.variant.input,
      attrs(syntax.attribute.placeholder := placeholder) ++ attributes,
      listeners,
      Styles(label = Style.Empty, input = style),
      lifecycle
    )
  }
}

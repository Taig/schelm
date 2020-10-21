package io.taig.schelm.material

import io.taig.schelm.css.data.Style
import io.taig.schelm.data.{Attributes, Lifecycle, Listeners}
import io.taig.schelm.dsl._

final case class MaterialInput[+F[_]](
    theme: MaterialTheme.Input,
    attributes: Attributes,
    listeners: Listeners[F],
    style: Style,
    lifecycle: Lifecycle.Element[F]
) extends Component[F, Nothing, Any] {
  val styles: Style = css(
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
  ) ++ style

  override def render: Widget[F, Nothing, Any] = input(attributes, listeners, styles, lifecycle)
}

object MaterialInput {
  def default[F[_]](
      label: String,
      placeholder: Option[String] = None,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialInput(
      theme.variant.input,
      attrs(syntax.attribute.placeholder := placeholder) ++ attributes,
      listeners,
      style,
      lifecycle
    )
  }
}

package io.taig.schelm.material

import io.taig.schelm.css.data.Style
import io.taig.schelm.data.{Attributes, Children, Lifecycle, Listeners, Tag}
import io.taig.schelm.dsl._

final case class MaterialTypography[+F[_], +Event, -Context](
    tag: Tag.Name,
    content: Widget[F, Event, Context],
    attributes: Attributes,
    listeners: Listeners[F],
    style: Style,
    lifecycle: Lifecycle.Element[F],
    theme: MaterialTheme.Font
) extends Component[F, Event, Context] {
  override def render: Widget[F, Event, Context] = {
    val styles = css(
      color := theme.color,
      fontFamily := theme.family,
      fontSize := theme.size,
      fontWeight := theme.weight,
      letterSpacing := theme.letterSpacing,
      lineHeight := theme.lineHeight,
      textTransform := theme.transform
    ) ++ fontSmoothing ++ style

    element(
      tag,
      attributes = attributes,
      listeners = listeners,
      style = styles,
      lifecycle,
      children = Children.of(content)
    )
  }
}

object MaterialTypography {
  def h1[F[_]](
      content: String,
      tag: Tag.Name = H1,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, text(content), attributes, listeners, style, lifecycle, theme.variant.typography.h1)
  }

  def h2[F[_]](
      content: String,
      tag: Tag.Name = H2,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, text(content), attributes, listeners, style, lifecycle, theme.variant.typography.h2)
  }

  def h3[F[_]](
      content: String,
      tag: Tag.Name = H3,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, text(content), attributes, listeners, style, lifecycle, theme.variant.typography.h3)
  }

  def h4[F[_]](
      content: String,
      tag: Tag.Name = H4,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, text(content), attributes, listeners, style, lifecycle, theme.variant.typography.h4)
  }

  def h5[F[_]](
      content: String,
      tag: Tag.Name = H5,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, text(content), attributes, listeners, style, lifecycle, theme.variant.typography.h5)
  }

  def h6[F[_]](
      content: String,
      tag: Tag.Name = H6,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, text(content), attributes, listeners, style, lifecycle, theme.variant.typography.h6)
  }

  def subtitle1[F[_]](
      content: String,
      tag: Tag.Name = H6,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, text(content), attributes, listeners, style, lifecycle, theme.variant.typography.subtitle1)
  }

  def subtitle2[F[_]](
      content: String,
      tag: Tag.Name = H6,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, text(content), attributes, listeners, style, lifecycle, theme.variant.typography.subtitle2)
  }

  def body1[F[_]](
      content: String,
      tag: Tag.Name = P,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, text(content), attributes, listeners, style, lifecycle, theme.variant.typography.body1)
  }

  def body2[F[_]](
      content: String,
      tag: Tag.Name = P,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, text(content), attributes, listeners, style, lifecycle, theme.variant.typography.body2)
  }

  def button[F[_]](
      content: String,
      tag: Tag.Name = Span,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, text(content), attributes, listeners, style, lifecycle, theme.variant.typography.button)
  }

  def caption[F[_]](
      content: String,
      tag: Tag.Name = Span,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, text(content), attributes, listeners, style, lifecycle, theme.variant.typography.caption)
  }

  def overline[F[_]](
      content: String,
      tag: Tag.Name = Span,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, text(content), attributes, listeners, style, lifecycle, theme.variant.typography.overline)
  }
}

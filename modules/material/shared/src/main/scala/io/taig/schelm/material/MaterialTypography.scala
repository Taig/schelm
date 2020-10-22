package io.taig.schelm.material

import io.taig.schelm.css.data.Style
import io.taig.schelm.data.{Attributes, Children, Lifecycle, Listeners, Tag}
import io.taig.schelm.dsl._

final case class MaterialTypography[+F[_], +Event, -Context](
    tag: Tag.Name,
    content: String,
    theme: MaterialTheme.Font,
    attributes: Attributes = Attributes.Empty,
    listeners: Listeners[F] = Listeners.Empty,
    style: Style = Style.Empty,
    lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
) extends Component[F, Event, Context] {
  val styles: Style = css(
    color := theme.color,
    fontFamily := theme.family,
    fontSize := theme.size,
    fontWeight := theme.weight,
    letterSpacing := theme.letterSpacing,
    lineHeight := theme.lineHeight,
    textTransform := theme.transform
  ) ++ fontSmoothing ++ style

  override val render: Widget[F, Event, Context] =
    element(tag, attributes, listeners, styles, lifecycle, children = Children.of(text(content)))
}

object MaterialTypography {
  def h1[F[_]](
      content: String,
      tag: Tag.Name = syntax.html.H1,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, content, theme.variant.typography.h1, attributes, listeners, style, lifecycle)
  }

  def h2[F[_]](
      content: String,
      tag: Tag.Name = syntax.html.H2,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, content, theme.variant.typography.h2, attributes, listeners, style, lifecycle)
  }

  def h3[F[_]](
      content: String,
      tag: Tag.Name = syntax.html.H3,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, content, theme.variant.typography.h3, attributes, listeners, style, lifecycle)
  }

  def h4[F[_]](
      content: String,
      tag: Tag.Name = syntax.html.H4,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, content, theme.variant.typography.h4, attributes, listeners, style, lifecycle)
  }

  def h5[F[_]](
      content: String,
      tag: Tag.Name = syntax.html.H5,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, content, theme.variant.typography.h5, attributes, listeners, style, lifecycle)
  }

  def h6[F[_]](
      content: String,
      tag: Tag.Name = syntax.html.H6,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, content, theme.variant.typography.h6, attributes, listeners, style, lifecycle)
  }

  def subtitle1[F[_]](
      content: String,
      tag: Tag.Name = syntax.html.H6,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, content, theme.variant.typography.subtitle1, attributes, listeners, style, lifecycle)
  }

  def subtitle2[F[_]](
      content: String,
      tag: Tag.Name = syntax.html.H6,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, content, theme.variant.typography.subtitle2, attributes, listeners, style, lifecycle)
  }

  def body1[F[_]](
      content: String,
      tag: Tag.Name = syntax.html.P,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, content, theme.variant.typography.body1, attributes, listeners, style, lifecycle)
  }

  def body2[F[_]](
      content: String,
      tag: Tag.Name = syntax.html.P,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, content, theme.variant.typography.body2, attributes, listeners, style, lifecycle)
  }

  def button[F[_]](
      content: String,
      tag: Tag.Name = syntax.html.Span,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, content, theme.variant.typography.button, attributes, listeners, style, lifecycle)
  }

  def caption[F[_]](
      content: String,
      tag: Tag.Name = syntax.html.Span,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, content, theme.variant.typography.caption, attributes, listeners, style, lifecycle)
  }

  def overline[F[_]](
      content: String,
      tag: Tag.Name = syntax.html.Span,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, content, theme.variant.typography.overline, attributes, listeners, style, lifecycle)
  }
}

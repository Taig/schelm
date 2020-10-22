package io.taig.schelm.material

import io.taig.schelm.data.{Children, Tag}
import io.taig.schelm.dsl._
import io.taig.schelm.dsl.data.Property

object MaterialTypography {
  def apply[F[_]](
      tag: Tag.Name,
      content: String,
      theme: MaterialTheme.Font,
      property: Property[F] = Property.Empty
  ): Widget[F, Nothing, Any] = {
    val style = css(
      color := theme.color,
      fontFamily := theme.family,
      fontSize := theme.size,
      fontWeight := theme.weight,
      letterSpacing := theme.letterSpacing,
      lineHeight := theme.lineHeight,
      textTransform := theme.transform
    ) ++ fontSmoothing

    element(tag, property.prependStyle(style), children = Children.of(text(content)))
  }

  def h1[F[_]](
      content: String,
      tag: Tag.Name = syntax.html.H1,
      property: Property[F] = Property.Empty
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, content, theme.variant.typography.h1, property)
  }

  def h2[F[_]](
      content: String,
      tag: Tag.Name = syntax.html.H2,
      property: Property[F] = Property.Empty
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, content, theme.variant.typography.h2, property)
  }

  def h3[F[_]](
      content: String,
      tag: Tag.Name = syntax.html.H3,
      property: Property[F] = Property.Empty
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, content, theme.variant.typography.h3, property)
  }

  def h4[F[_]](
      content: String,
      tag: Tag.Name = syntax.html.H4,
      property: Property[F] = Property.Empty
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, content, theme.variant.typography.h4, property)
  }

  def h5[F[_]](
      content: String,
      tag: Tag.Name = syntax.html.H5,
      property: Property[F] = Property.Empty
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, content, theme.variant.typography.h5, property)
  }

  def h6[F[_]](
      content: String,
      tag: Tag.Name = syntax.html.H6,
      property: Property[F] = Property.Empty
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, content, theme.variant.typography.h6, property)
  }

  def subtitle1[F[_]](
      content: String,
      tag: Tag.Name = syntax.html.H6,
      property: Property[F] = Property.Empty
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, content, theme.variant.typography.subtitle1, property)
  }

  def subtitle2[F[_]](
      content: String,
      tag: Tag.Name = syntax.html.H6,
      property: Property[F] = Property.Empty
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, content, theme.variant.typography.subtitle2, property)
  }

  def body1[F[_]](
      content: String,
      tag: Tag.Name = syntax.html.P,
      property: Property[F] = Property.Empty
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, content, theme.variant.typography.body1, property)
  }

  def body2[F[_]](
      content: String,
      tag: Tag.Name = syntax.html.P,
      property: Property[F] = Property.Empty
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, content, theme.variant.typography.body2, property)
  }

  def button[F[_]](
      content: String,
      tag: Tag.Name = syntax.html.Span,
      property: Property[F] = Property.Empty
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, content, theme.variant.typography.button, property)
  }

  def caption[F[_]](
      content: String,
      tag: Tag.Name = syntax.html.Span,
      property: Property[F] = Property.Empty
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, content, theme.variant.typography.caption, property)
  }

  def overline[F[_]](
      content: String,
      tag: Tag.Name = syntax.html.Span,
      property: Property[F] = Property.Empty
  ): Widget[F, Nothing, MaterialTheme] = contextual { theme =>
    MaterialTypography(tag, content, theme.variant.typography.overline, property)
  }
}

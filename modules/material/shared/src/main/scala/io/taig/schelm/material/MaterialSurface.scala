package io.taig.schelm.material

import io.taig.color.Color
import io.taig.schelm.data.{Children, Tag}
import io.taig.schelm.dsl._
import io.taig.schelm.dsl.data.Property

object MaterialSurface {
  def apply[F[_], Event, Context](
      background: Color,
      tag: Tag.Name = syntax.html.Body,
      property: Property[F] = Property.Empty,
      children: Children[Widget[F, Event, Context]] = Children.Empty
  ): Widget[F, Event, Context] = {
    val style = css(backgroundColor := background, margin := zero)
    element(tag, property.prependStyle(style), children)
  }

  def themed[F[_], Event](
      tag: Tag.Name = syntax.html.Body,
      property: Property[F] = Property.Empty,
      children: Children[Widget[F, Event, MaterialTheme]] = Children.Empty
  ): Widget[F, Event, MaterialTheme] = contextual { theme =>
    MaterialSurface[F, Event, MaterialTheme](
      background = theme.flavor.palette.surface.main,
      tag,
      property,
      children
    )
  }
}

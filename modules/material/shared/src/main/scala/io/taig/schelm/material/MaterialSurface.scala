package io.taig.schelm.material

import io.taig.color.Color
import io.taig.schelm.css.data.Style
import io.taig.schelm.data.{Attributes, Children, Lifecycle, Listeners, Tag}
import io.taig.schelm.dsl._

final case class MaterialSurface[+F[_], +Event, -Context](
    tag: Tag.Name,
    background: Color,
    attributes: Attributes,
    listeners: Listeners[F],
    style: Style,
    lifecycle: Lifecycle.Element[F],
    children: Children[Widget[F, Event, Context]]
) extends Component[F, Event, Context] {
  val styles: Style = css(backgroundColor := background, margin := zero) ++ style

  override def render: Widget[F, Event, Context] = element(tag, attributes, listeners, styles, lifecycle, children)
}

object MaterialSurface {
  def default[F[_], Event](
      children: Children[Widget[F, Event, MaterialTheme]],
      tag: Tag.Name = Body,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Event, MaterialTheme] = contextual { theme =>
    MaterialSurface[F, Event, MaterialTheme](
      tag,
      theme.flavor.palette.surface.main,
      attributes,
      listeners,
      style,
      lifecycle,
      children
    )
  }
}

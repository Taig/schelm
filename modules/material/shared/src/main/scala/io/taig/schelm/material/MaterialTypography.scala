package io.taig.schelm.material

import io.taig.schelm.css.data.Style
import io.taig.schelm.data.{Attributes, Children, Lifecycle, Listeners, Tag}
import io.taig.schelm.dsl._

final case class MaterialTypography[+F[_], +Event, -Context](
    tag: Tag.Name,
    content: Component[F, Event, Context],
    attributes: Attributes,
    listeners: Listeners[F],
    style: Style,
    lifecycle: Lifecycle.Element[F] = Lifecycle.Noop,
    theme: MaterialTheme.Font
) extends Component[F, Event, Context] {
  override def render: Widget[F, Event, Context] = {
    val styles = css(
      fontFamily := theme.family
    ) ++ style

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
  def p(value: String): Widget[Nothing, Nothing, MaterialTheme] = contextual { theme =>
    // todo extract current context / variant
    ???
  }
}

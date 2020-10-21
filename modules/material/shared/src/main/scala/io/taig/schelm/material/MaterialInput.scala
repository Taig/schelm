package io.taig.schelm.material

import io.taig.schelm.css.data.Style
import io.taig.schelm.data.{Attributes, Lifecycle, Listeners}
import io.taig.schelm.dsl._

final case class MaterialInput[+F[_]](
    attributes: Attributes,
    listeners: Listeners[F],
    style: Style,
    lifecycle: Lifecycle.Element[F]
) extends Component[F, Nothing, Any] {
  val styles = css() ++ style

  override def render: Widget[F, Nothing, Any] = input(attributes, listeners, styles, lifecycle)
}

object MaterialInput {
  def default[F[_]](
      label: String,
      value: Option[String] = None,
      placeholder: Option[String] = None,
      hint: Option[String] = None,
      error: Option[String] = None,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): MaterialInput[F] =
    MaterialInput(attributes, listeners, style, lifecycle)
}

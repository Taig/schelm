package io.taig.schelm.mdc

import io.taig.schelm.css.data.Style
import io.taig.schelm.data.{Attributes, Children}
import io.taig.schelm.dsl.data.DslNode
import io.taig.schelm.dsl._

final case class MdcIcon(name: String, label: String, element: String, attributes: Attributes, style: Style)
    extends DslNode.Component[Nothing, Nothing, Any] {
  override def render: DslNode[Nothing, Nothing, Any] =
    e.element(element)
      .apply(
        attributes = Attributes.of(a.cls := "material-icons", a.ariaLabel := label) ++ attributes,
        style = style,
        children = Children.of(text(name))
      )
}

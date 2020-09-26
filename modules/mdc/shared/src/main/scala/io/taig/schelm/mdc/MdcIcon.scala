package io.taig.schelm.mdc

import io.taig.schelm.data.{Attributes, Children}
import io.taig.schelm.dsl.data.DslWidget
import io.taig.schelm.dsl._

final case class MdcIcon(name: String, label: String, element: String, attributes: Attributes)
    extends DslWidget.Component[Nothing, Any] {
  override def render: DslWidget[Nothing, Any] =
    e.element(element)
      .apply(
        attributes = Attributes.of(a.cls := "material-icons", a.ariaLabel := label) ++ attributes,
        children = Children.of(text(name))
      )
}

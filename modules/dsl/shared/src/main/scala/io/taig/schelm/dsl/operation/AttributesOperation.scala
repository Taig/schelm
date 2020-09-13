package io.taig.schelm.dsl.operation

import cats.implicits._
import io.taig.schelm.css.data.CssNode
import io.taig.schelm.data.{Attributes, Component, Widget}
import io.taig.schelm.dsl.DslWidget

final class AttributesOperation[+F[_], -Context, +A](
    widget: Widget[Context, CssNode[Component[F, DslWidget[F, Context]]]],
    lift: Widget[Context, CssNode[Component[F, DslWidget[F, Context]]]] => A
) {
  def set(attributes: Attributes): A = patch(_ => attributes)

  def patch(f: Attributes => Attributes): A =
    lift(widget.map(_.map {
      case component @ Component.Element(tag, _, _) =>
        component.copy(tag = tag.copy(attributes = f(component.tag.attributes)))
      case component => component
    }))
}

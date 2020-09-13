package io.taig.schelm.dsl.operation

import cats.implicits._
import io.taig.schelm.css.data.CssNode
import io.taig.schelm.data.{Children, Component, Widget}
import io.taig.schelm.dsl.DslWidget

final class ChildrenOperation[+F[_], -Context, +A](
    widget: Widget[Context, CssNode[Component[F, DslWidget[F, Context]]]],
    lift: Widget[Context, CssNode[Component[F, DslWidget[F, Context]]]] => A
) {
//  def set(children: Children[DslWidget[F, Context]]): A = patch(_ => children)

  def patch[B](f: Children[B] => Children[DslWidget[F, Context]]): A =
    lift(widget.map(_.map {
      case component @ Component.Element(_, Component.Element.Type.Normal(children), _) =>
        component.copy(tpe = Component.Element.Type.Normal(f(children)))
      case component @ Component.Fragment(children, _) => component.copy(children = f(children))
      case component                                   => component
    }))
}

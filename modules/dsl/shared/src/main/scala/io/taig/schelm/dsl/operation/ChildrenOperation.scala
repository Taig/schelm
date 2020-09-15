package io.taig.schelm.dsl.operation

import cats.implicits._
import io.taig.schelm.css.data.CssNode
import io.taig.schelm.data.{Children, Component, Widget}
import io.taig.schelm.dsl.data.DslWidget

abstract class ChildrenOperation[+F[-_], -Context](
    widget: Widget[Context, CssNode[Component[DslWidget[Context]]]]
) {
  def lift[A <: Context](widget: Widget[A, CssNode[Component[DslWidget[A]]]]): F[A]

  def set[A <: Context](children: Children[DslWidget[A]]): F[A] = patch(_ => children)

  def patch[A <: Context](f: Children[DslWidget[Context]] => Children[DslWidget[A]]): F[A] =
    lift(widget.map(_.map {
      case component @ Component.Element(_, Component.Element.Type.Normal(children), _) =>
        component.copy(tpe = Component.Element.Type.Normal(f(children)))
      case component @ Component.Element(_, Component.Element.Type.Void, _) => component
      case component @ Component.Fragment(children, _)                      => component.copy(children = f(children))
      case component: Component.Text                                        => component
    }))
}

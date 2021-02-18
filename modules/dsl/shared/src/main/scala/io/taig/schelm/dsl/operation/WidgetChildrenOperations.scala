package io.taig.schelm.dsl.operation

import cats.syntax.all._
import io.taig.schelm.data.{Children, Node}
import io.taig.schelm.dsl.Widget

final class WidgetChildrenOperations[F[_], Event, Context](val widget: Widget[F, Event, Context]) extends AnyVal {
  def apply(children: Widget[F, Event, Context]*): Widget[F, Event, Context] = set(Children.from(children))

  def set(children: Children[Widget[F, Event, Context]]): Widget[F, Event, Context] = modify(_ => children)

  def append(children: Children[Widget[F, Event, Context]]): Widget[F, Event, Context] = modify(_ ++ children)

  def prepend(children: Children[Widget[F, Event, Context]]): Widget[F, Event, Context] = modify(children ++ _)

  def modify(f: Children[Widget[F, Event, Context]] => Children[Widget[F, Event, Context]]): Widget[F, Event, Context] =
    ???
//    Widget(widget.unfix.map(_.map(_.map(_.map(_.map {
//      case node: Node.Element[F, Widget[F, Event, Context]] =>
//        node.variant match {
//          case Node.Element.Variant.Normal(children) => node.copy(variant = Node.Element.Variant.Normal(f(children)))
//          case Node.Element.Variant.Void             => node
//        }
//      case node: Node.Fragment[Widget[F, Event, Context]] => node.copy(children = f(node.children))
//      case node: Node.Text[F]                             => node
//    })))))
}

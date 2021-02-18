package io.taig.schelm.dsl.operation

import cats.syntax.all._
import io.taig.schelm.data.{Attribute, Attributes, Node}
import io.taig.schelm.dsl.Widget

final class WidgetAttributesOperations[F[_], Event, Context](val widget: Widget[F, Event, Context]) extends AnyVal {
  def apply(attributes: Attribute*): Widget[F, Event, Context] = set(Attributes.from(attributes))

  def set(attributes: Attributes): Widget[F, Event, Context] = modify(_ => attributes)

  def append(attributes: Attributes): Widget[F, Event, Context] = modify(_ ++ attributes)

  def prepend(attributes: Attributes): Widget[F, Event, Context] = modify(attributes ++ _)

  def modify(f: Attributes => Attributes): Widget[F, Event, Context] = ???
//    Widget(widget.unfix.map(_.map(_.map(_.map(_.map {
//      case node: Node.Element[F, Widget[F, Event, Context]] =>
//        node.copy(tag = node.tag.copy(attributes = f(node.tag.attributes)))
//      case node => node
//    })))))
}

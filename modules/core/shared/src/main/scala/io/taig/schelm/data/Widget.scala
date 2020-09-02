package io.taig.schelm.data

import cats.implicits._

final case class Widget[Event, Context, Payload](
    render: Context => Node[Widget[Event, Context, Payload], Event],
    patch: Context => Context,
    payload: Payload
) {
  def apply(context: Context): Component[Event, Payload] = render(context) match {
    case node: Element.Normal[Widget[Event, Context, Payload], Event] =>
      val next = patch(context)
      val children = node.children.map(_.apply(next))
      Component(node.copy(children = children), payload)
    case node: Element.Void[Event] => Component(node, payload)
    case node: Fragment[Widget[Event, Context, Payload]] =>
      val next = patch(context)
      val children = node.children.map(_.apply(next))
      Component(node.copy(children = children), payload)
    case node: Text[Event] => Component(node, payload)
  }
}

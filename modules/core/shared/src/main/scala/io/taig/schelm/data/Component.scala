package io.taig.schelm.data

import cats.implicits._

final case class Component[Event, Payload](node: Node[Component[Event, Payload], Event], payload: Payload) {
  def html: Html[Event] = node match {
    case node: Element.Normal[Component[Event, Payload], Event] =>
      Html(node.copy(children = node.children.map(_.html)))
    case node: Element.Void[Event]                 => Html(node)
    case node: Fragment[Component[Event, Payload]] => Html(node.copy(children = node.children.map(_.html)))
    case node: Text[Event]                         => Html(node)
  }
}

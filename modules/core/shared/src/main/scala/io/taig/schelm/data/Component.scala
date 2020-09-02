package io.taig.schelm.data

import cats.{Applicative, Functor, Traverse}
import cats.implicits._

sealed abstract class Component[+Event, +Context, Payload] extends Product with Serializable

object Component {
  final case class Patch[Event, Context, Payload](f: Context => Context, component: Component[Event, Context, Payload])
  extends Component[Event, Context, Payload]

  final case class Pure[Event, Context, Payload](
      node: Node[Component[Event, Context, Payload], Event],
      payload: Payload
  ) extends Component[Event, Context, Payload]

  final case class Render[Event, Context, Payload](f: Context => Component[Event, Context, Payload])
    extends Component[Event, Context, Payload]

  def run[Event, Context, Payload](component: Component[Event, Context, Payload], context: Context): Component[Event, Unit, Payload] = component match {
    case Pure(node, payload) => Pure(node.map(Component.run(_, context)), payload)
  }
}

//(
//    render: Context => Node[Widget[Event, Context, Payload], Event],
//    patch: Context => Context,
//    payload: Payload
//) {
//  def apply(context: Context): Component[Event, Payload] = render(context) match {
//    case node: Element.Normal[Widget[Event, Context, Payload], Event] =>
//      val next = patch(context)
//      val children = node.children.map(_.apply(next))
//      Component(node.copy(children = children), payload)
//    case node: Element.Void[Event] => Component(node, payload)
//    case node: Fragment[Widget[Event, Context, Payload]] =>
//      val next = patch(context)
//      val children = node.children.map(_.apply(next))
//      Component(node.copy(children = children), payload)
//    case node: Text[Event] => Component(node, payload)
//  }
//}

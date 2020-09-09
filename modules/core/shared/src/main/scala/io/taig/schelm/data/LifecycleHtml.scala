package io.taig.schelm.data

final case class LifecycleHtml[F[_], Event, Node, Element <: Node, Text <: Node](
    node: Lifecycle[
      F,
      Component[Event, LifecycleHtml[F, Event, Node, Element, Text]],
      HtmlReference[Event, Node, Element, Text]
    ]
)

object LifecycleHtml {
//  def toHtml[F[_], Event](html: LifecycleHtml[F, Event]): Html[Event] = ???
//    Html(html.node.lifecycle.node.map(toHtml[F, Event]))
}

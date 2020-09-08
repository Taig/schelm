package io.taig.schelm.data

import cats.implicits._

final case class LifecycleHtml[F[_], Event](node: LifecycleNode[F, Event, LifecycleHtml[F, Event]])

object LifecycleHtml {
  def toHtml[F[_], Event](html: LifecycleHtml[F, Event]): Html[Event] = ???
//    Html(html.node.lifecycle.node.map(toHtml[F, Event]))
}

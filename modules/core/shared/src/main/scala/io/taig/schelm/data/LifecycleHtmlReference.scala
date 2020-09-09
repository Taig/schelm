package io.taig.schelm.data

import cats.implicits._

final case class LifecycleHtmlReference[F[_], +Event, +Node, +Element <: Node, +Text <: Node](
    // format: off
    lifecycle: Lifecycle[F, HtmlReference[Event, Node, Element, Text], NodeReference[Event, Element, Text, LifecycleHtmlReference[F, Event, Node, Element, Text]]]
    // format: on
) extends AnyVal {
  def toHtmlReference: HtmlReference[Event, Node, Element, Text] = HtmlReference(lifecycle.value.map(_.toHtmlReference))
}

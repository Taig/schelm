package io.taig.schelm.data

import scala.annotation.unchecked.uncheckedVariance

import cats.implicits._

final case class LifecycleHtml[+F[_], +Event, +Node, +Element <: Node, +Text <: Node](
    // format: off
    lifecycle: Lifecycle[F, HtmlReference[Event, Node, Element, Text], Component[Event, LifecycleHtml[F, Event, Node, Element, Text]]]
    // format: on
) extends AnyVal {
  def toHtml: Html[Event] = Html(lifecycle.value.map(_.toHtml))
}

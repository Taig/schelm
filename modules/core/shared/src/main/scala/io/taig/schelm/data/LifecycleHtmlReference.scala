//package io.taig.schelm.data
//
//import cats.implicits._
//
//final case class LifecycleHtmlReference[F[_], Node, Element <: Node, Text <: Node](
//    // format: off
//    lifecycle: Lifecycle[F, HtmlReference[ Node, Element, Text], NodeReference[ Element, Text, LifecycleHtmlReference[F,  Node, Element, Text]]]
//    // format: on
//) extends AnyVal {
//  def toHtmlReference: HtmlReference[ Node, Element, Text] = HtmlReference(lifecycle.value.map(_.toHtmlReference))
//}

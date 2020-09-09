//package io.taig.schelm.data
//
//import scala.annotation.unchecked.uncheckedVariance
//
//import cats.implicits._
//
//final case class LifecycleHtml[+F[_], Node, Element <: Node, Text <: Node](
//    // format: off
//    lifecycle: Lifecycle[F, HtmlReference[Node, Element, Text], Component[LifecycleHtml[F, Node, Element, Text]]]
//    // format: on
//) extends AnyVal {
//  def toHtml: Html = Html(lifecycle.value.map(_.toHtml))
//}

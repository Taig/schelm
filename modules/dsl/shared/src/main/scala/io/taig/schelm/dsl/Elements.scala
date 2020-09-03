package io.taig.schelm.dsl

import io.taig.schelm.css.data._
import io.taig.schelm.data._
import cats.implicits._

trait Elements {
  def element(label: String): StylesheetHtml[Element.Normal[Nothing, +*], Nothing] =
    StylesheetHtml.Unstyled(Element.Normal(Tag(label, Attributes.Empty, Listeners.Empty), Children.Empty))

  final case class MyHtml[Event](node: StylesheetHtml[Element.Normal[Event, +*], Event])

  def widget(element: StylesheetHtml[Element.Normal[Nothing, +*], Nothing]): Widget[Nothing, Nothing] = ???

  //  final val button: ElementNormalBuilder = element("button")
//
//  final val div: ElementNormalBuilder = element("div")
//
//  final val p: ElementNormalBuilder = element("p")
}

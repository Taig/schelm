package io.taig.schelm.playground

import io.taig.schelm.css.data.{StylesheetHtml, StylesheetWidget}
import io.taig.schelm.data._
import io.taig.schelm.dsl._

object Shared {
  final case class Theme()

  sealed abstract class Event extends Product with Serializable

  def widget(label: String): StylesheetWidget[Element.Normal[Event, +*], Event, Theme] =
    button(text(label))

  def component(label: String): StylesheetHtml[Node[Event, +*], Event] = StylesheetWidget.toStylesheetHtml(widget(label), Theme())

  def html(label: String): Html[Event] = StylesheetHtml.toHtml(component(label))._1
}

package io.taig.schelm.playground

import io.taig.schelm.css.data.{StylesheetHtml, StylesheetWidget}
import io.taig.schelm.data._
import io.taig.schelm.dsl._
import io.taig.schelm.dsl.data.DslWidget

object Shared {
  final case class Theme(background: String)

  sealed abstract class Event extends Product with Serializable

  object Event {
    final case object Click extends Event
  }

  def dslWidget(label: String): DslWidget[Element.Normal[Event, +*], Event, Theme] = contextual { theme =>
    div.apply(
      button
        .attrs(style := s"background-color: ${theme.background};")
        .on(click := Listener.Action.Pure(Event.Click))
        .apply(text(label)),
      hr
    )
  }

  def stylesheetWidget(label: String): StylesheetWidget[Event, Theme] =
    DslWidget.toStylesheetWidget(dslWidget(label))

  def stylesheetHtml(label: String): StylesheetHtml[Event] =
    StylesheetWidget.toStylesheetHtml(stylesheetWidget(label), Theme(background = "green"))

  def html(label: String): Html[Event] = StylesheetHtml.toHtml(stylesheetHtml(label))._1
}

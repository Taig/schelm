package io.taig.schelm.playground

import cats.Applicative
import cats.implicits._
import io.taig.schelm.algebra.{Handler, Schelm}
import io.taig.schelm.css.data.{StylesheetHtml, StylesheetWidget}
import io.taig.schelm.data._
import io.taig.schelm.dsl._

final case class Theme(background: String)

sealed abstract class Event extends Product with Serializable

object Event {
  final case object Click extends Event
}

final case class State(label: String)

final class MyHandler[F[_]: Applicative] extends Handler[F, State, Event, Nothing] {
  override def command(value: Nothing): F[Option[Event]] = none[Event].pure[F]

  override def event(state: State, event: Event): Result[State, Nothing] = event match {
    case Event.Click => Result(State(label = "Clicked (:").some, List.empty)
  }
}

object PlaygroundApp {
  val Initial: State = State(label = "Not clicked ):")

  def render(state: State): Html[Event] = html(state.label)

  def start[F[_]: Applicative, X](schelm: Schelm[F, Html[Event], Event, X], root: X): F[Unit] =
    schelm.start(root, Initial, render, new MyHandler[F])

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
    StylesheetWidget.toStylesheetHtml(stylesheetWidget(label), Theme(background = "red"))

  def html(label: String): Html[Event] = StylesheetHtml.toHtml(stylesheetHtml(label))._1
}

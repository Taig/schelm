package io.taig.schelm.playground

import cats.Applicative
import cats.implicits._
import io.taig.schelm.algebra.Handler
import io.taig.schelm.css.data.{CssHtml, CssWidget}
import io.taig.schelm.data._
import io.taig.schelm.dsl._
import io.taig.schelm.mdc.Chip

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

// TODO: think about hydration: render the html on the server, and send to client. Then render again on the client with
// same state, but afterwards remove all listeners. Then patch this version to only add the listeners, everything else
// should be in place already
object PlaygroundApp {
  val Initial: State = State(label = "Not clicked ):")

  def render(state: State): Html[Event] = html(state.label)

  def renderCss(state: State): CssHtml[Event] = stylesheetHtml(state.label)

  def cssWidget(label: String): CssWidget[Event, Theme] =
    contextual { theme =>
      div.apply(
        button
          .attrs(style := s"background-color: ${theme.background};", data("yolo") := label)
          .on(click := Listener.Action.Pure(Event.Click))
          .style(color := "white")
          .apply(text(label)),
        hr,
        button
          .attrs(style := s"background-color: white;")
          .on(click := Listener.Action.Pure(Event.Click))
          .style(color := theme.background)
          .apply(text(label)),
        Chip("hello google", selected = true),
        Chip("hello google", selected = false)
      )
    }

  def stylesheetHtml(label: String): CssHtml[Event] =
    CssWidget.toStylesheetHtml(cssWidget(label), Theme(background = "red"))

  def html(label: String): Html[Event] = CssHtml.toHtml(stylesheetHtml(label))._1
}

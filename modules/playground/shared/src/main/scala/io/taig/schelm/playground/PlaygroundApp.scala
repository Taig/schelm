package io.taig.schelm.playground

import cats.Applicative
import cats.effect.{ConcurrentEffect, IO}
import cats.implicits._
import io.taig.schelm.algebra.Handler
import io.taig.schelm.css.data.{CssHtml, CssWidget}
import io.taig.schelm.data._
import io.taig.schelm.dsl._
import io.taig.schelm.dsl.data.DslWidget
import io.taig.schelm.mdc.{Chip, ChipSet}

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

  def render(label: String): DslWidget[Theme] =
    ChipSet(chips =
      Children.of(
        Chip(label, selected = false, tabindex = 1),
        Chip("hello google", selected = false, tabindex = 2, icon = "event".some)
      )
    )
}

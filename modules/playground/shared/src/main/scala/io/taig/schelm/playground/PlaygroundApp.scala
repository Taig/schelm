package io.taig.schelm.playground

import cats.Applicative
import cats.implicits._
import io.taig.schelm.algebra.Handler
import io.taig.schelm.data._
import io.taig.schelm.dsl.data.DslWidget
import io.taig.schelm.mdc.{Chip, ChipSet}

final case class Theme(background: String)

sealed abstract class Event extends Product with Serializable

object Event {
  final case object Click extends Event
}

final case class State(label: String)

final class MyHandler[F[_]: Applicative] extends Handler[F, State, Event, Nothing] {
  override val command: Nothing => F[Option[Event]] = _ => none[Event].pure[F]

  override def event: (State, Event) => Result[State, Nothing] = {
    case (_, Event.Click) => Result(State(label = "Clicked (:").some, List.empty)
  }
}

// TODO: think about hydration: render the html on the server, and send to client. Then render again on the client with
// same state, but afterwards remove all listeners. Then patch this version to only add the listeners, everything else
// should be in place already
object PlaygroundApp {
  val Initial: State = State(label = "Not clicked ):")

  def render(label: String): DslWidget[Event, Theme] =
    ChipSet(chips =
      Children.of(
        Chip(label, tabindex = 1, icon = ("event", Chip.Icon.Position.Leading).some),
        Chip("hello google", tabindex = 2, icon = ("event", Chip.Icon.Position.Trailing).some)
      )
    )
}

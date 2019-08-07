package io.taig.schelm

import cats.Eq
import cats.effect.IO
import cats.implicits._
import io.taig.schelm.css._
import io.taig.schelm.dsl.{Dsl, Property}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object App extends Dsl {
  val html: Html[Event] =
    Html(
      Component.Fragment(
        Children.of(
          Html(Component.Text("Hello World")),
          Html(Component.Text("Hello World"))
        )
      )
    )

  def widget(state: State): Widget[Event] =
    css(
      div(
        id("asdf")
      )(
        p("Hello World"),
        br,
        button(
          style(cursor(pointer)),
          if (state.clicks < 5) onClick(Event.Increment(1)).some else None
        )(
          s"Does this work?: ${state.clicks}"
        )
      ),
      styles(
        if (state.clicks % 2 == 0) backgroundColor("greenyellow")
        else backgroundColor("yellow"),
        maxWidth(500.px),
        padding(5.px),
        &.after(
          backgroundColor("orangered"),
          content("''"),
          height(100.px),
          position(absolute),
          width(100.px)
        )
      )
    )

  val events: EventHandler[State, Event, Command] = {
    case (state, Event.Increment(value)) =>
      val update = state.copy(clicks = state.clicks + value)
      if (update.clicks === 10) Result(update, Command.Reset)
      else Result(update)
    case (state, Event.Decrement(value)) =>
      Result(state.copy(clicks = state.clicks - value))
    case (state, Event.Update(value)) =>
      Result(state.copy(text = value))
  }

  val commands: CommandHandler[IO, Command, Event] = {
    case Command.Reset =>
      IO.timer(ExecutionContext.global)
        .sleep(3.seconds)
        .as(Event.Decrement(10).some)
  }
}

final case class State(clicks: Int = 0, text: String = "")

sealed abstract class Event extends Product with Serializable

object Event {
  final case class Decrement(value: Int) extends Event
  final case class Increment(value: Int) extends Event
  final case class Update(value: String) extends Event

  implicit val eq: Eq[Event] = Eq.fromUniversalEquals
}

sealed abstract class Command extends Product with Serializable

object Command {
  final case object Reset extends Command
}

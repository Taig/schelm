package io.taig.schelm.playground

import cats.Eq
import cats.effect.IO
import cats.implicits._
import io.taig.schelm._
import io.taig.schelm.css._
import io.taig.schelm.dsl.widget._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object App {
  def widget(state: State): Widget[Event, Unit, Styles] =
    Widget.local(identity[Unit]) {
      Widget { _ =>
        div
          .attributes(id("yolo"))
          .styles(
            if (state.clicks % 2 == 0) backgroundColor("greenyellow")
            else backgroundColor("yellow"),
            maxWidth(500.px),
            padding(5.px)
          )
          .children(
            button
              .listeners(
                if (state.clicks < 5) onClick(Event.Increment(1))
                else onClick(Event.Increment(5))
              )
              .children(text("Click to play")),
            text(s"Does this work?: ${state.clicks}")
          )
      }
    }

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

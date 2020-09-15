package io.taig.schelm.mdc

import cats.Applicative
import cats.implicits._
import io.taig.schelm.algebra.Handler
import io.taig.schelm.data.Result

object MdcHandler {
  def apply[F[_]: Applicative, State, Event, Command](
      domain: Handler[F, State, Event, Command]
  ): Handler[F, State, MdcEvent[Event], MdcCommand[Command]] =
    new Handler[F, State, MdcEvent[Event], MdcCommand[Command]] {
      override val command: MdcCommand[Command] => F[Option[MdcEvent[Event]]] = {
        case MdcCommand.InitializeComponent(Component.Chip, element) => ???
        case MdcCommand.Domain(command)                              => domain.command(command).map(_.map(MdcEvent.Domain.apply))
      }

      override val event: (State, MdcEvent[Event]) => Result[State, MdcCommand[Command]] = {
        case (_, MdcEvent.ComponentMounted(component, reference)) => ???
        case (state, MdcEvent.Domain(command))                    => domain.event(state, command).map(MdcCommand.Domain.apply)
      }
    }
}

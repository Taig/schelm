package io.taig.schelm.mdc

import cats.Applicative
import cats.implicits._
import io.taig.schelm.algebra.Handler
import io.taig.schelm.data.{Platform, Result}
import io.taig.schelm.mdc.internal.MdcLifecycle

object MdcHandler {
  def apply[F[_]: Applicative, State, Event, Command](platform: Platform)(
      lifecycle: MdcLifecycle[F, platform.Element],
      domain: Handler[F, State, Event, Command]
  ): Handler[F, State, MdcEvent[platform.Element, Event], MdcCommand[platform.Element, Command]] =
    new Handler[F, State, MdcEvent[platform.Element, Event], MdcCommand[platform.Element, Command]] {
      override val command: MdcCommand[platform.Element, Command] => F[Option[MdcEvent[Element, Event]]] = {
        case MdcCommand.InitializeComponent(component, element) => lifecycle.initialize(component, element).as(none)
        case MdcCommand.Domain(command)                         => domain.command(command).map(_.map(MdcEvent.Domain.apply))
      }

      override val event: (State, MdcEvent[Element, Event]) => Result[State, MdcCommand[Element, Command]] = {
        case (_, MdcEvent.ComponentMounted(component, reference)) => ???
        case (state, MdcEvent.Domain(command))                    => domain.event(state, command).map(MdcCommand.Domain.apply)
      }
    }
}

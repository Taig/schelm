package io.taig.schelm.mdc

sealed abstract class MdcCommand[+Element, +Command] extends Product with Serializable

object MdcCommand {
  final case class Domain[Command](command: Command) extends MdcCommand[Nothing, Command]

  final case class InitializeComponent[Element](component: Component, element: Element)
      extends MdcCommand[Element, Nothing]
}

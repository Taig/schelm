package io.taig.schelm.mdc

sealed abstract class MdcCommand[+A] extends Product with Serializable

object MdcCommand {
  final case class Domain[A](command: A) extends MdcCommand[A]

  final case class InitializeComponent[Dom](component: Component, reference: Dom) extends MdcCommand[Nothing]
}

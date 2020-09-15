package io.taig.schelm.mdc

sealed abstract class MdcEvent[+A] extends Product with Serializable

object MdcEvent {
  final case class Domain[A](command: A) extends MdcEvent[A]

  final case class ComponentMounted[Dom](component: Component, reference: Dom) extends MdcEvent[Nothing]
}

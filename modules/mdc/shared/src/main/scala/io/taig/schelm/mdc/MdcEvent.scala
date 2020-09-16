package io.taig.schelm.mdc

sealed abstract class MdcEvent[+Event] extends Product with Serializable

object MdcEvent {
  final case class Domain[Event](event: Event) extends MdcEvent[Event]

  final case class ComponentMounted(component: Component, element: Any) extends MdcEvent[Nothing]
}

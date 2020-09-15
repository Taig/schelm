package io.taig.schelm.mdc

import io.taig.schelm.data.Platform

sealed abstract class MdcEvent[+Element, +Event] extends Product with Serializable

object MdcEvent {
  final case class Domain[Event](event: Event) extends MdcEvent[Nothing, Event]

  final case class ComponentMounted[Element](component: Component, element: Element) extends MdcEvent[Element, Nothing]
}
package io.taig.schelm

import cats.Monoid
import cats.implicits._

final case class Widget[+Event, Context, Payload](
    payload: Payload,
    render: Context => Component[Widget[Event, Context, Payload], Event]
) {
  def component(
      implicit ev: Unit =:= Context
  ): Component[Widget[Event, Context, Payload], Event] =
    render(unit)
}

object Widget {
  def pure[Event, Payload](
      payload: Payload,
      component: Component[Widget[Event, Unit, Payload], Event]
  ): Widget[Event, Unit, Payload] =
    Widget(payload, _ => component)

  def payload[A](widget: Widget[_, Unit, A])(implicit F: Monoid[A]): A =
    widget.component match {
      case component: Component.Element[Widget[_, Unit, A], _] =>
        widget.payload |+| component.children.values.map(payload[A]).combineAll
      case component: Component.Fragment[Widget[_, Unit, A]] =>
        widget.payload |+| component.children.values.map(payload[A]).combineAll
      case component: Component.Lazy[Widget[_, Unit, A]] =>
        widget.payload |+| payload(component.eval.value)
      case _: Component.Text => widget.payload
    }
}

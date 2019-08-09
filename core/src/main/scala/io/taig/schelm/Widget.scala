package io.taig.schelm

import cats.Monoid
import cats.implicits._

final case class Widget[+Event, Context, Payload](
    render: Context => (
        Component[Widget[Event, Context, Payload], Event],
        Payload
    )
) {
  def component(
      implicit ev: Unit =:= Context
  ): Component[Widget[Event, Context, Payload], Event] = render(unit)._1

  def payload(implicit ev: Unit =:= Context): Payload = render(unit)._2

  def merge(implicit F: Monoid[Payload], ev: Unit =:= Context): Payload =
    component match {
      case component: Component.Element[Widget[_, Context, Payload], _] =>
        payload |+| component.children.values
          .map(_.apply(unit).merge)
          .combineAll
      case component: Component.Fragment[Widget[_, Context, Payload]] =>
        payload |+| component.children.values
          .map(_.apply(unit).merge)
          .combineAll
      case component: Component.Lazy[Widget[_, Context, Payload]] =>
        payload |+| component.eval.value(unit).merge
      case _: Component.Text => payload
    }

  def apply(context: Context): Widget[Event, Unit, Payload] = {
    val (component, payload) = render(context)
    val update = component match {
      case Component.Element(name, attributes, listeners, children) =>
        Component.Element(
          name,
          attributes,
          listeners,
          children.map((_, widget) => widget(context))
        )
      case Component.Fragment(children) =>
        Component.Fragment(children.map((_, widget) => widget(context)))
      case Component.Lazy(eval, hash) =>
        Component.Lazy(eval.map(_.apply(context)), hash)
      case component: Component.Text => component
    }

    Widget.pure(update, payload)
  }
}

object Widget {
  def pure[Event, Payload](
      component: Component[Widget[Event, Unit, Payload], Event],
      payload: Payload
  ): Widget[Event, Unit, Payload] =
    Widget(_ => (component, payload))

  def empty[Event, Payload: Monoid](
      component: Component[Widget[Event, Unit, Payload], Event]
  ): Widget[Event, Unit, Payload] =
    pure(component, Monoid[Payload].empty)

  def of[Event, Context, Payload](
      f: Context => Widget[Event, Context, Payload]
  ): Widget[Event, Context, Payload] =
    Widget(context => f(context).render(context))
}

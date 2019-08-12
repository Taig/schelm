package io.taig.schelm

import cats.Monoid
import cats.implicits._

import scala.annotation.tailrec

sealed abstract class Widget[+Event, Context, Payload]
    extends Product
    with Serializable {
  def component(
      context: Context
  ): Component[Widget[Event, Context, Payload], Event] = this match {
    case Widget.Pure(component, _) => component
    case Widget.Render(apply)      => apply(context).component(context)
    case Widget.Local(f, widget)   => widget.component(f(context))
  }

  def component(
      implicit ev: Unit =:= Context
  ): Component[Widget[Event, Context, Payload], Event] = component(unit)

  def payload(context: Context): Payload = this match {
    case Widget.Pure(_, payload) => payload
    case Widget.Render(apply)    => apply(context).payload(context)
    case Widget.Local(f, widget) => widget.payload(f(context))
  }

  def payload(implicit ev: Unit =:= Context): Payload = payload(unit)

  def render(context: Context): Widget[Event, Unit, Payload] =
    Widget.render(context, this)

  def merge(implicit F: Monoid[Payload], ev: Unit =:= Context): Payload = {
    val payloads = component match {
      case component: Component.Element[Widget[_, Context, Payload], _] =>
        component.children.values.map(_.merge).combineAll
      case component: Component.Fragment[Widget[_, Context, Payload]] =>
        component.children.values.map(_.merge).combineAll
      case component: Component.Lazy[Widget[_, Context, Payload]] =>
        component.eval.value.merge
      case _: Component.Text => F.empty
    }

    payload(unit) |+| payloads
  }
}

object Widget {
  final case class Pure[+Event, Context, Payload](
      component: Component[Widget[Event, Context, Payload], Event],
      payload: Payload
  ) extends Widget[Event, Context, Payload]

  final case class Render[+Event, Context, Payload](
      apply: Context => Widget[Event, Context, Payload]
  ) extends Widget[Event, Context, Payload]

  final case class Local[+Event, Context, Payload](
      f: Context => Context,
      widget: Widget[Event, Context, Payload]
  ) extends Widget[Event, Context, Payload]

  def apply[Event, Context, Payload](
      apply: Context => Widget[Event, Context, Payload]
  ): Widget[Event, Context, Payload] =
    Render(apply)

  def pure[Event, Context, Payload](
      component: Component[Widget[Event, Context, Payload], Event],
      payload: Payload
  ): Widget[Event, Context, Payload] = Pure(component, payload)

  def empty[Event, Context, Payload: Monoid](
      component: Component[Widget[Event, Context, Payload], Event]
  ): Widget[Event, Context, Payload] =
    Pure(component, Monoid[Payload].empty)

  def local[Event, Context, Payload](
      f: Context => Context
  )(widget: Widget[Event, Context, Payload]): Widget[Event, Context, Payload] =
    Local(f, widget)

  @tailrec
  def render[Event, Context, Payload](
      context: Context,
      widget: Widget[Event, Context, Payload]
  ): Widget[Event, Unit, Payload] =
    widget match {
      case Pure(component, payload) => Pure(render(context, component), payload)
      case Render(apply)            => render(context, apply(context))
      case Local(f, widget)         => render(f(context), widget)
    }

  def render[Event, Context, Payload](
      context: Context,
      component: Component[Widget[Event, Context, Payload], Event]
  ): Component[Widget[Event, Unit, Payload], Event] =
    component match {
      case component: Component.Element[Widget[Event, Context, Payload], Event] =>
        val children = component.children.map { (_, child) =>
          render(context, child)
        }
        component.copy(children = children)
      case Component.Fragment(children) =>
        Component.Fragment(children.map((_, child) => render(context, child)))
      case Component.Lazy(eval, hash) =>
        Component.Lazy(eval.map(render(context, _)), hash)
      case component: Component.Text => component
    }

  def payload[Event, Context, Payload](
      widget: Widget[Event, Context, Payload]
  )(transform: Payload => Payload): Widget[Event, Context, Payload] =
    widget match {
      case Pure(component, payload) => Pure(component, transform(payload))
      case Render(apply) =>
        Render(context => payload(apply(context))(transform))
      case Local(f, widget) => Local(f, payload(widget)(transform))
    }

  def component[Event, Context, Payload](
      widget: Widget[Event, Context, Payload]
  )(
      transform: Component[Widget[Event, Context, Payload], Event] => Component[
        Widget[Event, Context, Payload],
        Event
      ]
  ): Widget[Event, Context, Payload] =
    widget match {
      case Pure(component, payload) => Pure(transform(component), payload)
      case Render(apply) =>
        Render(context => component(apply(context))(transform))
      case Local(f, widget) => Local(f, component(widget)(transform))
    }
}

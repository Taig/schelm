package io.taig.schelm

import cats.Monoid
import cats.implicits._

import scala.annotation.tailrec

sealed abstract class Widget[+Event, Context, Payload]
    extends Product
    with Serializable {
  @tailrec
  final def component(
      context: Context
  ): Component[Widget[Event, Context, Payload], Event] =
    this match {
      case Widget.Chain(apply) => apply(context).component(context)
      case Widget.Contextual(update, widget) =>
        widget.component(update(context))
      case Widget.Pure(component, _) => component
      case Widget.Render(apply)      => apply(context)._1
    }

  @tailrec
  final def payload(context: Context): Payload =
    this match {
      case Widget.Chain(apply)               => apply(context).payload(context)
      case Widget.Contextual(update, widget) => widget.payload(update(context))
      case Widget.Pure(_, payload)           => payload
      case Widget.Render(apply)              => apply(context)._2
    }

  final def merge(
      implicit F: Monoid[Payload],
      ev: Unit =:= Context
  ): Payload = {
    val payloads = component(unit) match {
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

  def render(context: Context): Widget[Event, Unit, Payload] =
    Widget.render(context, this)
}

object Widget {
  type Component[+Event, Context, Payload] =
    io.taig.schelm.Component[Widget[Event, Context, Payload], Event]

  type Update[Event, Context, Payload] =
    Component[Event, Context, Payload] => Component[Event, Context, Payload]

  final case class Chain[Event, Context, Payload](
      apply: Context => Widget[Event, Context, Payload]
  ) extends Widget[Event, Context, Payload]

  final case class Contextual[Event, Context, Payload](
      update: Context => Context,
      widget: Widget[Event, Context, Payload]
  ) extends Widget[Event, Context, Payload]

  final case class Pure[Event, Context, Payload](
      component: Component[Event, Context, Payload],
      payload: Payload
  ) extends Widget[Event, Context, Payload]

  final case class Render[Event, Context, Payload](
      apply: Context => (Component[Event, Context, Payload], Payload)
  ) extends Widget[Event, Context, Payload]

  def apply[Event, Context, Payload](
      f: Context => (Component[Event, Context, Payload], Payload)
  ): Widget[Event, Context, Payload] = Render(f)

  def of[Event, Context, Payload](
      f: Context => Widget[Event, Context, Payload]
  ): Widget[Event, Context, Payload] = Chain(f)

  def pure[Event, Context, Payload](
      component: Component[Event, Context, Payload],
      payload: Payload
  ): Widget[Event, Context, Payload] = Pure(component, payload)

  def empty[Event, Context, Payload: Monoid](
      component: Component[Event, Context, Payload]
  ): Widget[Event, Context, Payload] =
    pure(component, Monoid[Payload].empty)

  def component[Event, Context, Payload](
      widget: Widget[Event, Context, Payload]
  )(
      f: Update[Event, Context, Payload]
  ): Widget[Event, Context, Payload] = widget match {
    case Chain(apply)               => Chain(context => component(apply(context))(f))
    case Contextual(update, widget) => Contextual(update, component(widget)(f))
    case Pure(component, payload)   => Pure(f(component), payload)
    case Render(apply)              => Render(apply(_).leftMap(f))
  }

  def payload[Event, Context, Payload](
      widget: Widget[Event, Context, Payload]
  )(f: Payload => Payload): Widget[Event, Context, Payload] = widget match {
    case Chain(apply)               => Chain(context => payload(apply(context))(f))
    case Contextual(update, widget) => Contextual(update, payload(widget)(f))
    case Pure(component, payload)   => Pure(component, f(payload))
    case Render(apply)              => Render(apply(_).map(f))
  }

  @tailrec
  def render[Event, Context, Payload](
      context: Context,
      widget: Widget[Event, Context, Payload]
  ): Widget[Event, Unit, Payload] =
    widget match {
      case Chain(apply)               => render(context, apply(context))
      case Contextual(update, widget) => render(update(context), widget)
      case Pure(component, payload)   => Pure(render(context, component), payload)
      case Render(apply) =>
        val (component, payload) = apply(context)
        Pure(render(context, component), payload)
    }

  def render[Event, Context, Payload](
      context: Context,
      component: Component[Event, Context, Payload]
  ): Component[Event, Unit, Payload] =
    component match {
      case Component.Element(name, attributes, listeners, children) =>
        Component.Element(
          name,
          attributes,
          listeners,
          children.map((_, child) => render(context, child))
        )
      case Component.Fragment(children) =>
        Component.Fragment(children.map((_, child) => render(context, child)))
      case Component.Lazy(eval, hash) =>
        Component.Lazy(eval.map(render(context, _)), hash)
      case component: Component.Text => component
    }
}

package io.taig.schelm.dsl.syntax

import io.taig.schelm.css.data.{Css, Style}
import io.taig.schelm.data._
import io.taig.schelm.dsl.Widget
import io.taig.schelm.redux.algebra.EventManager
import io.taig.schelm.redux.data.Redux

trait component {
  final def element[F[_], Event, Context](
      name: Tag.Name,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop,
      children: Children[Widget[F, Event, Context]] = Children.Empty
  ): Widget[F, Event, Context] = {
    val tag = Tag(name, attributes, listeners)
    val variant = Node.Element.Variant.Normal(children)
    val node = Node.Element(tag, variant, lifecycle)
    Widget.Pure(Redux.Pure(Contextual.Pure(State.Stateless(Css(node, style)))))
  }

  final def void[F[_], Event, Context](
      name: Tag.Name,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Widget[F, Event, Context] = {
    val tag = Tag(name, attributes, listeners)
    val node = Node.Element(tag, Node.Element.Variant.Void, lifecycle)
    Widget.Pure(Redux.Pure(Contextual.Pure(State.Stateless(Css(node, style)))))
  }

  final def fragment[F[_], Event, Context](
      children: Children[Widget[F, Event, Context]] = Children.Empty
  ): Widget[F, Event, Context] =
    Widget.Pure(Redux.Pure(Contextual.Pure(State.Stateless(Css.unstyled(Node.Fragment(children))))))

  final def text[F[_]](
      value: String,
      listeners: Listeners[F] = Listeners.Empty,
      lifecycle: Lifecycle.Text[F] = Lifecycle.Noop
  ): Widget[F, Nothing, Any] =
    Widget.Pure(Redux.Pure(Contextual.Pure(State.Stateless(Css.unstyled(Node.Text(value, listeners, lifecycle))))))

  final def contextual[F[_], Event, Context](f: Context => Widget[F, Event, Any]): Widget[F, Event, Context] =
    Widget.Pure(
      Redux.Render { (events: EventManager[F, Event]) =>
        Contextual.Render(context => Contextual.run(context)(Redux.run(events)(Widget.run(f(context)))))
      }
    )

  final def eventful[F[_], Event, Context](
      f: EventManager[F, Event] => Widget[F, Nothing, Context]
  ): Widget[F, Event, Context] = ???
}

object component extends component

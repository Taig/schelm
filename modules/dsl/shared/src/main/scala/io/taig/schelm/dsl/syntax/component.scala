package io.taig.schelm.dsl.syntax

import io.taig.schelm.css.data.Css
import io.taig.schelm.data._
import io.taig.schelm.dsl.Widget
import io.taig.schelm.dsl.data.Property
import io.taig.schelm.redux.algebra.EventManager
import io.taig.schelm.redux.data.Redux

trait component {
  final def element[F[_], Event, Context](
      name: Tag.Name,
      property: Property[F] = Property.Empty,
      children: Children[Widget[F, Event, Context]] = Children.Empty
  ): Widget[F, Event, Context] = {
    val tag = Tag(name, property.attributes, property.listeners)
    val variant = Node.Element.Variant.Normal(children)
    val node = Node.Element(tag, variant, property.lifecycle)
    Widget(Redux.Pure(Contextual.Pure(State.Stateless(Css(node, property.style)))))
  }

  final def void[F[_], Event, Context](
      name: Tag.Name,
      property: Property[F] = Property.Empty
  ): Widget[F, Event, Context] = {
    val tag = Tag(name, property.attributes, property.listeners)
    val node = Node.Element(tag, Node.Element.Variant.Void, property.lifecycle)
    Widget(Redux.Pure(Contextual.Pure(State.Stateless(Css(node, property.style)))))
  }

  final def fragment[F[_], Event, Context](
      children: Children[Widget[F, Event, Context]] = Children.Empty
  ): Widget[F, Event, Context] =
    Widget(Redux.Pure(Contextual.Pure(State.Stateless(Css.unstyled(Node.Fragment(children))))))

  final def text[F[_]](
      value: String,
      listeners: Listeners[F] = Listeners.Empty,
      lifecycle: Lifecycle.Text[F] = Lifecycle.Noop
  ): Widget[F, Nothing, Any] =
    Widget(Redux.Pure(Contextual.Pure(State.Stateless(Css.unstyled(Node.Text(value, listeners, lifecycle))))))

  final def contextual[F[_], Event, Context](f: Context => Widget[F, Event, Context]): Widget[F, Event, Context] =
    Widget(
      Redux.Render { (events: EventManager[F, Event]) =>
        Contextual.Render(context => Contextual.run(context)(Redux.run(events)(f(context).redux)))
      }
    )

  final def eventful[F[_], Event, Context](
      f: EventManager[F, Event] => Widget[F, Nothing, Context]
  ): Widget[F, Event, Context] = ???

  def indexed[F[_], Event, Context](children: Widget[F, Event, Context]*): Children[Widget[F, Event, Context]] =
    Children.Indexed(children.toVector)
}

object component extends component

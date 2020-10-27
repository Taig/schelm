package io.taig.schelm.dsl.syntax

import scala.collection.immutable.VectorMap

import io.taig.schelm.css.data.Css
import io.taig.schelm.data._
import io.taig.schelm.dsl.Widget
import io.taig.schelm.dsl.data.{Property, StateUpdate}
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
    Widget(Redux.Pure(Contextual.Pure(Stateless(Css(node, property.style)))))
  }

  final def void[F[_], Event, Context](
      name: Tag.Name,
      property: Property[F] = Property.Empty
  ): Widget[F, Event, Context] = {
    val tag = Tag(name, property.attributes, property.listeners)
    val node = Node.Element(tag, Node.Element.Variant.Void, property.lifecycle)
    Widget(Redux.Pure(Contextual.Pure(Stateless(Css(node, property.style)))))
  }

  final def fragment[F[_], Event, Context](
      children: Children[Widget[F, Event, Context]] = Children.Empty
  ): Widget[F, Event, Context] =
    Widget(Redux.Pure(Contextual.Pure(Stateless(Css.unstyled(Node.Fragment(children))))))

  final def text[F[_]](
      value: String,
      listeners: Listeners[F] = Listeners.Empty,
      lifecycle: Lifecycle.Text[F] = Lifecycle.Noop
  ): Widget[F, Nothing, Any] =
    Widget(Redux.Pure(Contextual.Pure(Stateless(Css.unstyled(Node.Text(value, listeners, lifecycle))))))

  def indexed[F[_], Event, Context](children: Widget[F, Event, Context]*): Children[Widget[F, Event, Context]] =
    Children.from(children)

  def identified[F[_], Event, Context](
      children: (String, Widget[F, Event, Context])*
  ): Children[Widget[F, Event, Context]] =
    Children.Identified(children.to(VectorMap))

  final def contextual[F[_], Event, Context](f: Context => Widget[F, Event, Context]): Widget[F, Event, Context] =
    Widget(
      Redux.Render { (events: EventManager[F, Event]) =>
        Contextual.Render(context => Contextual.run(context)(Redux.run(events)(f(context).redux)))
      }
    )

  final def eventful[F[_], Event]: EventfulBuilder[F, Event] = new EventfulBuilder[F, Event]

  final class EventfulBuilder[F[_], Event] {
    def apply[Context](
        f: EventManager[F, Event] => Widget[F, Nothing, Context]
    ): Widget[F, Event, Context] =
      Widget(
        Redux.Render { (events: EventManager[F, Event]) => Redux.run(events)(f(events).redux) }
      )
  }

  final def stateful[F[_]] = new StatefulBuilder[F]

  final class StatefulBuilder[F[_]] {
    def apply[Event, Context, A](
        initial: A
    )(f: StateUpdate[F, A] => Widget[F, Event, Context]): Widget[F, Event, Context] =
      Widget(
        Redux.Render { (events: EventManager[F, Event]) =>
          Contextual.Render { (context: Context) =>
            Stateful(initial, { (update: (A => A) => F[Unit], current: A) =>
              Contextual.run(context)(Redux.run(events)(f(new StateUpdate(current, update)).redux))
            })
          }
        }
      )
  }
}

object component extends component

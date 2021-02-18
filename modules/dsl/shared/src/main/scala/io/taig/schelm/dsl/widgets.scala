package io.taig.schelm.dsl

import cats.syntax.all._
import io.taig.schelm.css.data.Css
import io.taig.schelm.data._
import io.taig.schelm.redux.algebra.EventManager
import io.taig.schelm.redux.data.Redux

trait widgets {
  def node[F[_], Event, Context]: Node[F, Widget[F, Event, Context]] => Widget[F, Event, Context] = node => ???
//    Widget(Redux.pure(Contextual.pure(State.stateless(Namespace.anonymous(Css.unstyled(node))))))

  def tag(name: Tag.Name): Widget[Nothing, Nothing, Any] =
    node(
      Node.Element(
        Tag(name, Attributes.Empty, Listeners.Empty),
        Node.Element.Variant.Normal(Children.Empty),
        Lifecycle.Noop
      )
    )

  def void(name: Tag.Name): Widget[Nothing, Nothing, Any] =
    node(
      Node.Element(
        Tag(name, Attributes.Empty, Listeners.Empty),
        Node.Element.Variant.Void,
        Lifecycle.Noop
      )
    )

  val fragment: Widget[Nothing, Nothing, Any] = node(Node.Fragment(Children.Empty))

  def text(value: String): Widget[Nothing, Nothing, Any] = node(Node.Text(value, Listeners.Empty))

  def redux[F[_], Event, Context](f: EventManager[F, Event] => Widget[F, Event, Context]): Widget[F, Event, Context] =
    ???
//    Widget(Redux((events: EventManager[F, Event]) => f(events).unfix.provide(events)))

  def contextual[Context]: ContextualBuilder[Context] = new ContextualBuilder[Context]

  final class ContextualBuilder[Context] {
    def apply[F[_], Event](f: Context => Widget[F, Event, Context]): Widget[F, Event, Context] = ???
//      Widget(
//        Redux((events: EventManager[F, Event]) =>
//          Contextual.use(context => f(context).unfix.provide(events).provide(context))
//        )
//      )
  }

  def identified[F[_], Event, Context](
      identifier: Identifier,
      widget: Widget[F, Event, Context]
  ): Widget[F, Event, Context] = ???
//    Widget(widget.unfix.map(_.map(_.map(namespace => Namespace(identifier, namespace)))))

  def stateful[A](identifier: Identifier, default: A): StatefulBuilder[A] =
    new StatefulBuilder(identifier, default)

  final class StatefulBuilder[A](identifier: Identifier, default: A) {
    def apply[F[_], Event, Context](f: StateUpdate[F, A] => Widget[F, Event, Context]): Widget[F, Event, Context] = ???
//      Widget(
//        Redux { (events: EventManager[F, Event]) =>
//          Contextual.use { context =>
//            State.stateful(
//              identifier,
//              default,
//              (update: (A => A) => F[Unit], current: A) =>
//                f(new StateUpdate(current, update)).unfix.provide(events).provide(context)
//            )
//          }
//        }
//      )
  }
}

object widgets extends widgets

package io.taig.schelm.dsl.syntax

import io.taig.schelm.css.data.{Css, Style}
import io.taig.schelm.data.{Attributes, Children, Lifecycle, Listeners, Node, State, Tag, Widget}
import io.taig.schelm.dsl.util.{ModifyContext, ModifyRedux}
import io.taig.schelm.dsl.{Component, Element, Fragment, Text}
import io.taig.schelm.redux.algebra.EventManager
import io.taig.schelm.redux.data.Redux

trait component {
  final def element[F[_], Event, Context](
      name: String,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop,
      children: Children[Component[F, Event, Context]] = Children.Empty
  ): Element[F, Event, Context] = {
    val tag = Tag(name, attributes, listeners)
    val variant = Node.Element.Variant.Normal(children)
    val node = Node.Element(tag, variant, lifecycle)
    Element(Redux.Pure(Widget.Pure(State.Stateless(Css(node, style)))))
  }

  final def void[F[_], Event, Context](
      name: String,
      attributes: Attributes = Attributes.Empty,
      listeners: Listeners[F] = Listeners.Empty,
      style: Style = Style.Empty,
      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
  ): Element[F, Event, Context] = {
    val tag = Tag(name, attributes, listeners)
    val node = Node.Element(tag, Node.Element.Variant.Void, lifecycle)
    Element(Redux.Pure(Widget.Pure(State.Stateless(Css(node, style)))))
  }

  final def fragment[F[_], Event, Context](
      children: Children[Component[F, Event, Context]] = Children.Empty
  ): Component[F, Event, Context] =
    Fragment(Redux.Pure(Widget.Pure(State.Stateless(Css.unstyled(Node.Fragment(children))))))

  final def text[F[_]](
      value: String,
      listeners: Listeners[F] = Listeners.Empty,
      lifecycle: Lifecycle.Text[F] = Lifecycle.Noop
  ): Component[F, Nothing, Any] =
    Text(Redux.Pure(Widget.Pure(State.Stateless(Css.unstyled(Node.Text(value, listeners, lifecycle))))))

  final def contextual[F[_], Context](f: Context => F[Any])(implicit context: ModifyContext[F]): F[Context] =
    context.contextual(f)

  final def eventful[F[_[_], _, _], G[_], Event, Context](
      f: EventManager[G, Event] => F[Nothing, Nothing, Context]
  )(implicit redux: ModifyRedux[F[*[_], *, Context]]): F[G, Event, Context] =
    redux.eventful(f)
}

object component extends component

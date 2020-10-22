package io.taig.schelm.dsl

import cats.implicits._
import io.taig.schelm.css.data.{Css, Style}
import io.taig.schelm.data._
import io.taig.schelm.redux.data.Redux

final case class Widget[+F[_], +Event, -Context](
    redux: Redux[F, Event, Contextual[Context, State[F, Css[Node[F, Listeners[F], Widget[F, Event, Context]]]]]]
) extends AnyVal {
  def modifyAttributes(f: Attributes => Attributes): Widget[F, Event, Context] =
    Widget(redux.map(_.map(_.map(_.map(_.modifyAttributes(f))))))

  def modifyListeners[G[A] >: F[A]](f: Listeners[G] => Listeners[G]): Widget[G, Event, Context] =
    Widget(redux.map(_.map(_.map(_.map(_.modifyListeners(f))))))

  def modifyChildren[G[A] >: F[A], E >: Event, C <: Context](
      f: Children[Widget[G, E, C]] => Children[Widget[G, E, C]]
  ): Widget[G, E, C] =
    Widget(redux.map(_.map(_.map(_.map(_.modifyChildren(f))))))

  def modifyStyle(f: Style => Style): Widget[F, Event, Context] = Widget(redux.map(_.map(_.map(_.modifyStyle(f)))))
}

object Widget {
  implicit def fromString(value: String): Widget[Nothing, Nothing, Any] = syntax.component.text(value)
}

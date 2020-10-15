package io.taig.schelm.dsl.util

import io.taig.schelm.data.Widget
import io.taig.schelm.dsl.{Element, Fragment, Text}
import io.taig.schelm.dsl.util.Tagged.@@
import io.taig.schelm.redux.algebra.EventManager
import io.taig.schelm.redux.data.Redux
import simulacrum.typeclass

@typeclass
trait ModifyContext[F[_]] {
  def contextual[A](f: A => F[Any]): F[A]
}

object ModifyContext {
  implicit def text[F[_], Event]: ModifyContext[Text[F, Event, *]] = new ModifyContext[Text[F, Event, *]] {
    override def contextual[A](f: A => Text[F, Event, Any]): Text[F, Event, A] =
      Text(Redux.Render { events: EventManager[F, Event] =>
        Widget.Render((context: A) => Widget.run(context)(Redux.run(events)(f(context).render)))
      })
  }

  implicit def fragment[F[_], Event]: ModifyContext[Fragment[F, Event, *]] = new ModifyContext[Fragment[F, Event, *]] {
    override def contextual[A](f: A => Fragment[F, Event, Any]): Fragment[F, Event, A] =
      Fragment(Redux.Render { events: EventManager[F, Event] =>
        Widget.Render((context: A) => Widget.run(context)(Redux.run(events)(f(context).render)))
      })
  }

  implicit def element[F[_], Event]: ModifyContext[Element[F, Event, *]] =
    new ModifyContext[Element[F, Event, *]] {
      override def contextual[A](f: A => Element[F, Event, Any]): Element[F, Event, A] =
        Element(Redux.Render { events: EventManager[F, Event] =>
          Widget.Render((context: A) => Widget.run(context)(Redux.run(events)(f(context).render)))
        })
    }

  implicit def tagged[F[_], Marker](implicit modify: => ModifyContext[F]): ModifyContext[λ[A => F[A] @@ Marker]] =
    new ModifyContext[λ[A => F[A] @@ Marker]] {
      override def contextual[A](f: A => F[Any] @@ Marker): F[A] @@ Marker =
        Tagged(modify.contextual(f))
    }
}

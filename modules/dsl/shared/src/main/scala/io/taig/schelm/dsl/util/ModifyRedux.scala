package io.taig.schelm.dsl.util

import io.taig.schelm.dsl.{Element, Fragment, Text}
import io.taig.schelm.dsl.util.Tagged.@@
import io.taig.schelm.redux.algebra.EventManager
import io.taig.schelm.redux.data.Redux
import simulacrum.typeclass

@typeclass
trait ModifyRedux[F[_[_], _]] {
  def eventful[G[_], A](f: EventManager[G, A] => F[Nothing, Nothing]): F[G, A]
}

object ModifyRedux {
  implicit def text[Context]: ModifyRedux[Text[*[_], *, Context]] = new ModifyRedux[Text[*[_], *, Context]] {
    override def eventful[G[_], A](f: EventManager[G, A] => Text[Nothing, Nothing, Context]): Text[G, A, Context] =
      Text(Redux.Render((events: EventManager[G, A]) => Redux.run(events)(f(events).render)))
  }

  implicit def fragment[Context]: ModifyRedux[Fragment[*[_], *, Context]] =
    new ModifyRedux[Fragment[*[_], *, Context]] {
      override def eventful[G[_], A](
          f: EventManager[G, A] => Fragment[Nothing, Nothing, Context]
      ): Fragment[G, A, Context] =
        Fragment(Redux.Render((events: EventManager[G, A]) => Redux.run(events)(f(events).render)))
    }

  implicit def element[Context]: ModifyRedux[Element[*[_], *, Context]] = new ModifyRedux[Element[*[_], *, Context]] {
    override def eventful[G[_], A](
        f: EventManager[G, A] => Element[Nothing, Nothing, Context]
    ): Element[G, A, Context] =
      Element(Redux.Render((events: EventManager[G, A]) => Redux.run(events)(f(events).render)))
  }

  implicit def tagged[F[_[_], _, _], Context, Marker](
      implicit modify: => ModifyRedux[λ[(G[_], A) => F[G, A, Context]]]
  ): ModifyRedux[λ[(G[_], A) => F[G, A, Context] @@ Marker]] =
    new ModifyRedux[λ[(G[_], A) => F[G, A, Context] @@ Marker]] {
      override def eventful[G[_], A](
          f: EventManager[G, A] => F[Nothing, Nothing, Context] @@ Marker
      ): F[G, A, Context] @@ Marker =
        Tagged(modify.eventful(f))
    }
}

package io.taig.schelm.redux.interpreter

import cats.data.Kleisli
import cats.implicits._
import cats.{~>, Applicative, Functor, Id}
import io.taig.schelm.algebra.Renderer
import io.taig.schelm.data.Fix
import io.taig.schelm.redux.algebra.EventManager
import io.taig.schelm.redux.data.Redux
import io.taig.schelm.util.FunctionKs

object ReduxRenderer {
  def pure[F[_], G[_]: Functor, Event](manager: EventManager[F, Event]): Fix[λ[A => Redux[F, Event, G[A]]]] => Fix[G] =
    redux => Fix(redux.unfix.run(manager).map(pure(manager)))

  def id[F[_], G[_]: Functor, Event]
      : Renderer[Kleisli[Id, EventManager[F, Event], *], Fix[λ[A => Redux[F, Event, G[A]]]], Fix[G]] =
    Kleisli(redux => Kleisli[Id, EventManager[F, Event], Fix[G]](manager => pure[F, G, Event](manager).apply(redux)))

  def apply[F[_]: Applicative, G[_]: Functor, Event]
      : Renderer[Kleisli[F, EventManager[F, Event], *], Fix[λ[A => Redux[F, Event, G[A]]]], Fix[G]] = {
    val lift = new (Kleisli[Id, EventManager[F, Event], *] ~> Kleisli[F, EventManager[F, Event], *]) {
      override def apply[A](fa: Kleisli[Id, EventManager[F, Event], A]): Kleisli[F, EventManager[F, Event], A] =
        fa.mapK(FunctionKs.liftId[F])
    }

    id[F, G, Event].mapK(lift)
  }

  def fromEventManager[F[_]: Applicative, G[_]: Functor, Event](
      manager: EventManager[F, Event]
  ): Renderer[F, Fix[λ[A => Redux[F, Event, G[A]]]], Fix[G]] =
    ReduxRenderer[F, G, Event].mapK(Kleisli.applyK(manager))
}

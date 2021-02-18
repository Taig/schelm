package io.taig.schelm.redux.instances

import cats.Functor
import io.taig.schelm.redux.data.Redux
import io.taig.schelm.redux.data.Redux.{Pure, Render}

trait ReduxInstances {
  implicit def reduxFunctor[F[_], Event]: Functor[Redux[F, Event, *]] = new Functor[Redux[F, Event, *]] {
    override def map[A, B](fa: Redux[F, Event, A])(f: A => B): Redux[F, Event, B] = fa match {
      case Redux.Pure(value)                => Pure(f(value))
      case redux: Redux.Render[F, Event, A] => Render[F, Event, B](manager => f(redux.f(manager)))
    }
  }
}

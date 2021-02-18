package io.taig.schelm.instance

import cats.Functor
import cats.syntax.all._
import io.taig.schelm.data.Contextual

trait ContextualInstances {
  implicit def contextualFunctor[A]: Functor[Contextual[A, *]] = new Functor[Contextual[A, *]] {
    override def map[B, C](fa: Contextual[A, B])(f: B => C): Contextual[A, C] =
      fa match {
        case Contextual.Local(context, render) => Contextual.Local(context, render.map(f))
        case Contextual.Pure(value)            => Contextual.Pure(f(value))
      }
  }
}

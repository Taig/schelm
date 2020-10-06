package io.taig.schelm.algebra

import cats.implicits._
import cats.{~>, Applicative, Monad}

abstract class Renderer[F[_], -View, Reference] { self =>
  def render(view: View): F[Reference]

  final def andThen[A](renderer: Renderer[F, Reference, A])(implicit F: Monad[F]): Renderer[F, View, A] =
    new Renderer[F, View, A] {
      override def render(view: View): F[A] = self.render(view).flatMap(renderer.render)
    }

  final def mapK[G[_]](fk: F ~> G): Renderer[G, View, Reference] = new Renderer[G, View, Reference] {
    override def render(view: View): G[Reference] = fk(self.render(view))
  }
}

object Renderer {
  def identity[F[_]: Applicative, A]: Renderer[F, A, A] = new Renderer[F, A, A] {
    override def render(view: A): F[A] = view.pure[F]
  }
}

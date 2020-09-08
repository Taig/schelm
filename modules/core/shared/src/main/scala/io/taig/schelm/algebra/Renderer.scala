package io.taig.schelm.algebra

import cats.implicits._
import cats.{~>, Functor}

abstract class Renderer[F[_], View, Structure] { self =>
  def render(view: View): F[Structure]

  final def xmap[A, B](f: A => View)(g: (A, Structure) => B)(implicit F: Functor[F]): Renderer[F, A, B] =
    new Renderer[F, A, B] {
      override def render(view: A): F[B] = self.render(f(view)).map(g(view, _))
    }

  def tapWith[C, AA <: View](f: (AA, Structure) => C)(implicit F: Functor[F]): Renderer[F, AA, C] =
    new Renderer[F, AA, C] {
      override def render(view: AA): F[C] =
        self.render(view).map(structure => f(view, structure))
    }

  final def first[C](implicit F: Functor[F]): Renderer[F, (View, C), (Structure, C)] =
    new Renderer[F, (View, C), (Structure, C)] {
      override def render(view: (View, C)): F[(Structure, C)] =
        self.render(view._1).map(structure => (structure, view._2))
    }

  final def map[A](f: Structure => A)(implicit F: Functor[F]): Renderer[F, View, A] = new Renderer[F, View, A] {
    override def render(view: View): F[A] = self.render(view).map(f)
  }

  final def contramap[A](f: A => View)(implicit F: Functor[F]): Renderer[F, A, Structure] =
    new Renderer[F, A, Structure] {
      override def render(view: A): F[Structure] = self.render(f(view))
    }

  final def mapK[G[_]](fK: F ~> G): Renderer[G, View, Structure] =
    new Renderer[G, View, Structure] {
      override def render(view: View): G[Structure] = fK(self.render(view))
    }
}

object Renderer {
//  implicit def functor[F[_]: Functor, View]: Functor[Renderer[F, View, *]] = new Functor[Renderer[F, View, *]] {
//    override def map[A, B](fa: Renderer[F, View, A])(f: A => B): Renderer[F, View, B] =
//      fa.map(f)
//  }
//
//  implicit def contravariant[F[_]: Functor, Structure]: Contravariant[Renderer[F, *, Structure]] =
//    new Contravariant[Renderer[F, *, Structure]] {
//      override def contramap[A, B](fa: Renderer[F, A, Structure])(f: B => A): Renderer[F, B, Structure] =
//        fa.contramap(f)
//    }
}

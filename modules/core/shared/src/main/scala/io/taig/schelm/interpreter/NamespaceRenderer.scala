package io.taig.schelm.interpreter

import scala.annotation.tailrec

import cats.Applicative
import cats.data.Kleisli
import cats.implicits._
import io.taig.schelm.algebra.Renderer
import io.taig.schelm.data.{Identification, Namespace}

object NamespaceRenderer {
  def apply[F[_]: Applicative, A, B](
      renderer: Renderer[F, (Identification, A), B]
  ): Renderer[Kleisli[F, Identification, *], Namespace[A], Namespace[B]] = {
    @tailrec
    def render(identification: Identification, namespace: Namespace[A]): F[Namespace[B]] = namespace match {
      case namespace: Namespace.Anonymous[A] =>
        renderer.run((identification, namespace.value)).map(Namespace.Anonymous[B])
      case namespace: Namespace.Identified[A] => render(identification / namespace.identifier, namespace.namespace)
    }

    Kleisli(namespace => Kleisli(identification => render(identification, namespace)))
  }
}

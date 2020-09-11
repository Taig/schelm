package io.taig.schelm.data

import cats.Applicative
import io.taig.schelm.algebra.Dom

object Callback {
  abstract class Element[F[_]] {
    def apply(dom: Dom[F])(reference: dom.Element): F[Unit]
  }

  abstract class Fragment[F[_]] {
    def apply(dom: Dom[F])(reference: List[dom.Node]): F[Unit]
  }

  abstract class Text[F[_]] {
    def apply(dom: Dom[F])(reference: dom.Text): F[Unit]
  }

  object Text {
    def noop[F[_]](implicit F: Applicative[F]): Text[F] = new Text[F] {
      override def apply(dom: Dom[F])(reference: dom.Text): F[Unit] = F.unit
    }
  }
}

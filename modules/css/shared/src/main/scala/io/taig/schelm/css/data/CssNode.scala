package io.taig.schelm.css.data

import cats.{Applicative, Eval, Traverse}
import cats.implicits._

final case class CssNode[+A](component: A, style: Style)

object CssNode {
  implicit val traverse: Traverse[CssNode] = new Traverse[CssNode] {
    override def traverse[G[_]: Applicative, A, B](fa: CssNode[A])(f: A => G[B]): G[CssNode[B]] =
      f(fa.component).map(component => fa.copy(component = component))

    override def foldLeft[A, B](fa: CssNode[A], b: B)(f: (B, A) => B): B = f(b, fa.component)

    override def foldRight[A, B](fa: CssNode[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = f(fa.component, lb)
  }
}

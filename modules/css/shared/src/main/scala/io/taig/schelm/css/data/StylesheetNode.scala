package io.taig.schelm.css.data

import cats.implicits._
import cats.{Applicative, Eval, Traverse}

final case class StylesheetNode[+Event, +A](value: A, stylesheet: Stylesheet)

object StylesheetNode {
  implicit def traverse[Event]: Traverse[StylesheetNode[Event, *]] = new Traverse[StylesheetNode[Event, *]] {
    override def traverse[G[_]: Applicative, A, B](
        fa: StylesheetNode[Event, A]
    )(f: A => G[B]): G[StylesheetNode[Event, B]] =
      f(fa.value).map(node => fa.copy(value = node))

    override def foldLeft[A, B](fa: StylesheetNode[Event, A], b: B)(f: (B, A) => B): B = f(b, fa.value)

    override def foldRight[A, B](fa: StylesheetNode[Event, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      f(fa.value, lb)
  }
}

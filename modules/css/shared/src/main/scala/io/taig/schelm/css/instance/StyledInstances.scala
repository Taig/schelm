package io.taig.schelm.css.instance

import cats.{Applicative, Eval, Traverse}
import cats.syntax.all._
import io.taig.schelm.css.data.Css

trait StyledInstances {
  implicit val styledTraverse: Traverse[Css] = new Traverse[Css] {
    override def traverse[G[_]: Applicative, A, B](fa: Css[A])(f: A => G[B]): G[Css[B]] =
      f(fa.value).map(value => fa.copy(value = value))

    override def foldLeft[A, B](fa: Css[A], b: B)(f: (B, A) => B): B = f(b, fa.value)

    override def foldRight[A, B](fa: Css[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = f(fa.value, lb)
  }
}

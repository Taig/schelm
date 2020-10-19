package io.taig.schelm.css.data

import cats.implicits._
import cats.{Applicative, Eval, Traverse}

final case class Css[+A](value: A, style: Style) {
  def modifyStyle(f: Style => Style): Css[A] = copy(style = f(style))
}

object Css {
  def unstyled[A](value: A): Css[A] = Css(value, Style.Empty)

  implicit val traverse: Traverse[Css] = new Traverse[Css] {
    override def traverse[G[_]: Applicative, A, B](fa: Css[A])(f: A => G[B]): G[Css[B]] =
      f(fa.value).map(component => fa.copy(value = component))

    override def foldLeft[A, B](fa: Css[A], b: B)(f: (B, A) => B): B = f(b, fa.value)

    override def foldRight[A, B](fa: Css[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = f(fa.value, lb)
  }
}

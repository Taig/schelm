package io.taig.schelm.data

import cats._
import cats.implicits._

final case class Tag[+Listeners](name: Tag.Name, attributes: Attributes, listeners: Listeners)

object Tag {
  final case class Name(value: String) extends AnyVal

  implicit val traverse: Traverse[Tag] = new Traverse[Tag] {
    override def traverse[G[_]: Applicative, A, B](fa: Tag[A])(f: A => G[B]): G[Tag[B]] =
      f(fa.listeners).map(listeners => fa.copy(listeners = listeners))

    override def foldLeft[A, B](fa: Tag[A], b: B)(f: (B, A) => B): B = f(b, fa.listeners)

    override def foldRight[A, B](fa: Tag[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = f(fa.listeners, lb)
  }
}

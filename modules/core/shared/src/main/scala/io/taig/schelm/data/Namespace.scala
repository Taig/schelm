package io.taig.schelm.data

import cats._
import cats.implicits._

sealed abstract class Namespace[+A] extends Product with Serializable {
  final def mapWithIdentifier[G[_], B](f: (Option[Identifier], A) => B): Namespace[B] = ???

  final def mapWithIdentification[G[_], B](f: (Identification, A) => B): Namespace[B] = ???

  final def traverseWithIdentification[G[_], B](f: (Identification, A) => G[B]): Namespace[B] = ???
}

object Namespace {
  final case class Identified[A](identifier: Identifier, namespace: Namespace[A]) extends Namespace[A]
  final case class Anonymous[A](value: A) extends Namespace[A]

  implicit val traverse: Traverse[Namespace] = new Traverse[Namespace] {
    override def traverse[G[_]: Applicative, A, B](namespace: Namespace[A])(f: A => G[B]): G[Namespace[B]] =
      namespace match {
        case Identified(identifier, namespace) => traverse(namespace)(f).map(Identified(identifier, _))
        case Anonymous(value)                  => f(value).map(Anonymous[B])
      }

    override def foldLeft[A, B](namespace: Namespace[A], b: B)(f: (B, A) => B): B = ???

    override def foldRight[A, B](namespace: Namespace[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = ???
  }
}

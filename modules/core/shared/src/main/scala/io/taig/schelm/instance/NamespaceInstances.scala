package io.taig.schelm.instance

import scala.annotation.tailrec

import cats.syntax.all._
import cats.{Applicative, Eval, Traverse}
import io.taig.schelm.data.Namespace

trait NamespaceInstances {
  implicit val namespaceTraverse: Traverse[Namespace] = new Traverse[Namespace] {
    override def traverse[G[_]: Applicative, A, B](fa: Namespace[A])(f: A => G[B]): G[Namespace[B]] =
      fa match {
        case Namespace.Anonymous(value) => f(value).map(Namespace.Anonymous[B])
        case Namespace.Identified(identifier, namespace) =>
          traverse(namespace)(f).map(Namespace.Identified(identifier, _))
      }

    @tailrec
    override def foldLeft[A, B](fa: Namespace[A], b: B)(f: (B, A) => B): B = fa match {
      case Namespace.Anonymous(value)         => f(b, value)
      case Namespace.Identified(_, namespace) => foldLeft(namespace, b)(f)
    }

    @tailrec
    override def foldRight[A, B](fa: Namespace[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = fa match {
      case Namespace.Anonymous(value)         => f(value, lb)
      case Namespace.Identified(_, namespace) => foldRight(namespace, lb)(f)
    }
  }
}

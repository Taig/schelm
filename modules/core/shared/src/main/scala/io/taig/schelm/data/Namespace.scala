package io.taig.schelm.data

import scala.annotation.tailrec

import cats._
import cats.implicits._
import io.taig.schelm.util.NamespaceTraverse

sealed abstract class Namespace[+A] extends Product with Serializable

object Namespace {
  final case class Identified[A](identifier: Key.Identifier, namespace: Namespace[A]) extends Namespace[A]
  final case class Anonymous[A](value: A) extends Namespace[A]

  implicit val traverse: NamespaceTraverse[Namespace] = new NamespaceTraverse[Namespace] {
    override def map[A, B](fa: Namespace[A])(f: A => B): Namespace[B] = fa match {
      case identified @ Identified(_, namespace) => identified.copy(namespace = map(namespace)(f))
      case anonymous @ Anonymous(value)          => anonymous.copy(value = f(value))
    }

    override def mapWithIdentification[A, B](fa: Namespace[A])(f: (Identification, A) => B): Namespace[B] =
      mapWithIdentification(fa, Identification.Empty, f)

    def mapWithIdentification[A, B](
        fa: Namespace[A],
        prefix: Identification,
        f: (Identification, A) => B
    ): Namespace[B] = fa match {
      case identified @ Identified(identifier, namespace) =>
        identified.copy(namespace = mapWithIdentification(namespace, prefix / identifier, f))
      case anonymous @ Anonymous(value) => anonymous.copy(value = f(prefix, value))
    }

    override def mapWithPath[A, B](fa: Namespace[A])(f: (Path, A) => B): Namespace[B] = ???

    def mapWithPath[A, B](fa: Namespace[A], prefix: Path, f: (Path, A) => B): Namespace[B] = fa match {
      case Identified(identifier, namespace)        => ???
      case Identified(identifier, Anonymous(value)) => ???
      case Anonymous(value)                         => ???
    }

    override def traverse[G[_]: Applicative, A, B](fa: Namespace[A])(f: A => G[B]): G[Namespace[B]] = fa match {
      case identified @ Identified(_, namespace) =>
        traverse(namespace)(f).map(namespace => identified.copy(namespace = namespace))
      case anonymous @ Anonymous(value) => f(value).map(value => anonymous.copy(value = value))
    }

    override def traverseWithIdentification[G[_]: Applicative, A, B](
        fa: Namespace[A]
    )(f: (Identification, A) => G[B]): G[Namespace[B]] =
      traverseWithIdentification(fa, Identification.Empty, f)

    def traverseWithIdentification[G[_]: Applicative, A, B](
        fa: Namespace[A],
        prefix: Identification,
        f: (Identification, A) => G[B]
    ): G[Namespace[B]] = fa match {
      case identified @ Identified(identifier, namespace) =>
        traverseWithIdentification(namespace, prefix / identifier, f).map(namespace =>
          identified.copy(namespace = namespace)
        )
      case anonymous @ Anonymous(value) => f(prefix, value).map(value => anonymous.copy(value = value))
    }

    @tailrec
    override def foldLeft[A, B](fa: Namespace[A], b: B)(f: (B, A) => B): B = fa match {
      case Identified(_, namespace) => foldLeft(namespace, b)(f)
      case Anonymous(value)         => f(b, value)
    }

    override def foldLeftWithIdentification[A, B](fa: Namespace[A], b: B)(f: (B, Identification, A) => B): B =
      foldLeftWithIdentification(fa, Identification.Empty, b, f)

    @tailrec
    def foldLeftWithIdentification[A, B](
        fa: Namespace[A],
        prefix: Identification,
        b: B,
        f: (B, Identification, A) => B
    ): B =
      fa match {
        case Identified(identifier, namespace) => foldLeftWithIdentification(namespace, prefix / identifier, b, f)
        case Anonymous(value)                  => f(b, prefix, value)
      }

    @tailrec
    override def foldRight[A, B](fa: Namespace[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = fa match {
      case Identified(_, namespace) => foldRight(namespace, lb)(f)
      case Anonymous(value)         => f(value, lb)
    }
  }
}

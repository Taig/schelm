package io.taig.schelm.data

import cats.effect.kernel.Resource
import io.taig.schelm.algebra.Dom

sealed abstract class Lifecycle[+F[_], -A] extends Product with Serializable {
  override def toString: String = this match {
    case Lifecycle.Node(mount)     => s"{mount: $mount}"
    case Lifecycle.Fragment(mount) => s"{mount: $mount}"
    case Lifecycle.Noop            => "{}"
  }
}

object Lifecycle {
  final case class Node[F[_], A](mount: A => Resource[F, Unit]) extends Lifecycle[F, A]

  final case class Fragment[F[_]](mount: Resource[F, Unit]) extends Lifecycle[F, Any]

  case object Noop extends Lifecycle[Nothing, Any]

  type Element[F[_]] = Lifecycle[F, Dom.Element]

  type Text[F[_]] = Lifecycle[F, Dom.Text]
}

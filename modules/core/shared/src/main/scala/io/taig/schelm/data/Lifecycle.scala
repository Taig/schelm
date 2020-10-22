package io.taig.schelm.data

import cats.effect.Resource
import io.taig.schelm.algebra.Dom

final case class Lifecycle[+F[_], -A](mount: Option[A => Resource[F, Unit]]) {
  def ++[G[B] >: F[B], B <: A](lifecycle: Lifecycle[G, B]): Lifecycle[G, B] =
    (mount, lifecycle.mount) match {
      case (Some(first), Some(second)) => Lifecycle(mount = Some(a => first(a).flatMap(_ => second(a))))
      case (mount, None)               => Lifecycle(mount)
      case (None, mount)               => Lifecycle(mount)
    }
}

object Lifecycle {
  type Element[+F[_]] = Lifecycle[F, Dom.Element]

  type Text[+F[_]] = Lifecycle[F, Dom.Text]

  val Noop: Lifecycle[Nothing, Any] = Lifecycle(mount = None)
}

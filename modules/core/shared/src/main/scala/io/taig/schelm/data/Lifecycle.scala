package io.taig.schelm.data

import io.taig.schelm.algebra.Dom

final case class Lifecycle[+F[_], -A](mounted: Option[A => F[Unit]], unmount: Option[A => F[Unit]])

final case class Lifecycle2[+F[_], -A](mounted: A => F[Unit])

object Lifecycle {
  type Element[F[_]] = Lifecycle[F, Dom.Element]

  type Text[F[_]] = Lifecycle[F, Dom.Text]

  val Noop: Lifecycle[Nothing, Any] = Lifecycle(None, None)
}

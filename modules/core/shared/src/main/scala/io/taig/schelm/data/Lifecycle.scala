package io.taig.schelm.data

import io.taig.schelm.algebra.Dom

final case class Lifecycle[+F[_], A](mounted: Option[A => F[Unit]], unmount: Option[A => F[Unit]])

object Lifecycle {
  type Element[F[_]] = Lifecycle[F, Dom.Element]

  object Element {
    val Noop: Lifecycle.Element[Nothing] = Lifecycle(None, None)
  }

  type Text[F[_]] = Lifecycle[F, Dom.Text]

  object Text {
    val Noop: Lifecycle.Text[Nothing] = Lifecycle(None, None)
  }
}

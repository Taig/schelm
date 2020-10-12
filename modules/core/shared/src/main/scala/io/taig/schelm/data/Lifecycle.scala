package io.taig.schelm.data

import cats.effect.Resource
import io.taig.schelm.algebra.Dom

final case class Lifecycle[+F[_], -A](mount: Option[A => Resource[F, Unit]])

object Lifecycle {
  type Element[+F[_]] = Lifecycle[F, Dom.Element]

  type Text[+F[_]] = Lifecycle[F, Dom.Text]

  val Noop: Lifecycle[Nothing, Any] = Lifecycle(mount = None)
}

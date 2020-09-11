package io.taig.schelm.data

final case class Lifecycle[+A](mounted: Option[A], unmount: Option[A])

object Lifecycle {
  val Empty: Lifecycle[Nothing] = Lifecycle(None, None)
}

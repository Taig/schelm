package io.taig.schelm.data

final case class Html[F[_]](component: Component[F, Html[F]]) extends AnyVal

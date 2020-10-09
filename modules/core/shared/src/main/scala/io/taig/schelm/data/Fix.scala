package io.taig.schelm.data

final case class Fix[F[_]](unfix: F[Fix[F]]) extends AnyVal

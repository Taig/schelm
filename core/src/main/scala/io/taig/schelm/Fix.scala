package io.taig.schelm

final case class Fix[+F[+_]](value: F[Fix[F]])

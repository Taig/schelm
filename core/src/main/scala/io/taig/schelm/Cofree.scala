package io.taig.schelm

final case class Cofree[+F[+_], +A](head: A, tail: F[Cofree[F, A]])

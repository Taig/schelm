package io.taig.schelm.data

final case class Html[F[_]](node: Node[F, Html[F]])

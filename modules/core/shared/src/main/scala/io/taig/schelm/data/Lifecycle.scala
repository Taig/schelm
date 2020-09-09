package io.taig.schelm.data

import io.taig.schelm.algebra.Dom

final case class Lifecycle[F[_], A, +B](
    node: B,
    mounted: (Dom[F], A) => F[Unit],
    unmount: (Dom[F], A) => F[Unit]
)

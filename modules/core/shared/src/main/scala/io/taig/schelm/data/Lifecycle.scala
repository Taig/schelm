package io.taig.schelm.data

import io.taig.schelm.algebra.Dom

final case class Lifecycle[F[_], Event, A](
    node: A,
    mounted: (Dom[F], Reference[Event, A]) => F[Unit],
    unmount: (Dom[F], Reference[Event, A]) => F[Unit]
)

object Lifecycle {
//  implicit def traverse[F[_], Event, Structure <: Granularity[Dom.Node]]: Traverse[Lifecycle[F, Event, Structure, *]] =
//    ???
}

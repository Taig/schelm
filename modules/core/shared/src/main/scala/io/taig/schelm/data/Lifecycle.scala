package io.taig.schelm.data

final case class Lifecycle[A](mounted: A, unmount: A)

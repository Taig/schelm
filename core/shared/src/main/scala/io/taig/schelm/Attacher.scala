package io.taig.schelm

abstract class Attacher[F[_], Node, Component] {
  def attach(parent: Node, child: Component): F[Unit]
}

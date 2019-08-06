package io.taig.schelm

abstract class Attacher[F[_], Node, Reference] {
  def attach(node: Node, reference: Reference): F[Unit]
}

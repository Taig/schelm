package io.taig.schelm

abstract class Attacher[F[_], Reference] {
  def attach(container: Element, reference: Reference): F[Unit]
}

package io.taig.schelm.algebra

abstract class Attacher[F[_], Structure, Element] {
  def attach(parent: Element, structure: Structure): F[Unit]
}

package io.taig.schelm.algebra

abstract class Attacher[F[_], Structure] {
  def attach(structure: Structure): F[Unit]
}

package io.taig.schelm.algebra

abstract class Attacher[F[+_], -Structure, +Target] {
  def attach(structure: Structure): F[Target]
}

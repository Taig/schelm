package io.taig.schelm.algebra

abstract class Attacher[F[_], Reference, Target] {
  def attach(reference: Reference): F[Target]
}

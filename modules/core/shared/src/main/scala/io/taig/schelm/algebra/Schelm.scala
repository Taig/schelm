package io.taig.schelm.algebra

import cats.effect.Resource

abstract class Schelm[F[_], View] {
  def start(app: View): Resource[F, Unit]
}

package io.taig.schelm.algebra

import io.taig.schelm.data.Identifier

abstract class Ids[F[_]] {
  def next: F[Identifier]
}

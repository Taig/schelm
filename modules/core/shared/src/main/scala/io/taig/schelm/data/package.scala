package io.taig.schelm

import cats.effect.Resource

package object data {
  type Lifecycle[F[_], -A] = A => Resource[F, Unit]
}

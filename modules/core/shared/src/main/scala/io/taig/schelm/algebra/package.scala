package io.taig.schelm

import cats.data.Kleisli

package object algebra {
  type Attacher[F[_], Reference, Target] = Kleisli[F, Reference, Target]

  type Renderer[F[_], -View, Reference] = Kleisli[F, View, Reference]
}

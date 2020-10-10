package io.taig.schelm

import cats.Applicative
import cats.data.Kleisli
import cats.implicits._

package object algebra {
  type Attacher[F[_], Reference, Target] = Kleisli[F, Reference, Target]

  type Renderer[F[_], -View, Reference] = Kleisli[F, View, Reference]

  type Patcher[F[_], Reference, Diff] = Kleisli[F, (Reference, Diff), Reference]

  object Patcher {
    def noop[F[_]: Applicative, Reference, Diff]: Patcher[F, Reference, Diff] = Kleisli {
      case (reference, _) => reference.pure[F]
    }
  }
}

package io.taig.schelm

import cats.Applicative
import cats.data.Kleisli
import cats.implicits._

package object algebra {
  type Attacher[F[_], Hydrated, Target] = Kleisli[F, Hydrated, Target]

  type Differ[F[_], Rendered, Diff] = Kleisli[F, (Rendered, Rendered), Option[Diff]]

  type Hydrater[F[_], Rendered, Hydrated] = Kleisli[F, Rendered, Hydrated]

  type Renderer[F[_], View, Rendered] = Kleisli[F, View, Rendered]

  type Patcher[F[_], Hydrated, Diff] = Kleisli[F, (Hydrated, Diff), Hydrated]

  object Patcher {
    def apply[F[_], Hydrated, Diff](f: (Hydrated, Diff) => F[Hydrated]): Patcher[F, Hydrated, Diff] =
      Kleisli(f.tupled)

    def noop[F[_]: Applicative, Hydrated, Diff]: Patcher[F, Hydrated, Diff] = Kleisli {
      case (reference, _) => reference.pure[F]
    }
  }
}

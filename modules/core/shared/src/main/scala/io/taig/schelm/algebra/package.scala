package io.taig.schelm

import cats.data.Kleisli

package object algebra {
  type Attacher[F[_], Hydrated, Target] = Kleisli[F, Hydrated, Target]

  type Differ[F[_], Rendered, Diff] = Kleisli[F, (Rendered, Rendered), Option[Diff]]

  type Hydrater[F[_], Rendered, Hydrated] = Kleisli[F, Rendered, Hydrated]

  type Renderer[F[_], View, Rendered] = Kleisli[F, View, Rendered]

  type Patcher[F[_], Hydrated, Diff] = Kleisli[F, (Hydrated, Diff), Hydrated]
}

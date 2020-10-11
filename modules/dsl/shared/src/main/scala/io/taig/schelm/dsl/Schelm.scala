package io.taig.schelm.dsl

import cats.effect.Resource
import io.taig.schelm.dsl.data.DslNode

final class Schelm[F[_]] {
  def start[Event, Context, State](
      initial: State,
      context: State => Context,
      render: State => DslNode[F, Event, Context]
  ): Resource[F, Unit] =
    for {
      reference <- Resource.liftF(structurer.andThen(renderer).run(app).flatMap(attacher.run))
    } yield ???
}

package io.taig.schelm.interpreter

import cats.effect.implicits._
import cats.effect.{Concurrent, Resource}
import cats.implicits._
import io.taig.schelm.algebra._
import io.taig.schelm.util.PathTraversal
import io.taig.schelm.util.PathTraversal.ops._

final class DomSchelm[F[_], View, Structure, Reference, Target: PathTraversal, Diff](
    states: StateManager[F, Structure],
    structurer: Renderer[F, View, Structure],
    renderer: Renderer[F, Structure, Reference],
    attacher: Attacher[F, Reference, Target],
    differ: Differ[Structure, Diff],
    patcher: Patcher[F, Target, Diff]
)(extract: Target => Structure)(implicit F: Concurrent[F])
    extends Schelm[F, View] {
  override def start(app: View): Resource[F, Unit] =
    for {
      reference <- Resource.liftF(structurer.andThen(renderer).run(app).flatMap(attacher.run))
      _ <- states.subscription
        .evalScan(reference) { (reference, update) =>
          reference.modify[F](update.path) { reference =>
            differ
              .diff(extract(reference), update.structure)
              .traverse(patcher.run(reference, _))
              .map(_.getOrElse(reference))
          }
        }
        .compile
        .drain
        .onError { throwable =>
          F.delay {
            System.err.println("Failed to apply state update")
            throwable.printStackTrace()
          }
        }
        .background
    } yield ()
}

object DomSchelm {
  def apply[F[_]: Concurrent, View, Structure, Reference, Target: PathTraversal, Diff](
      states: StateManager[F, Structure],
      structurer: Renderer[F, View, Structure],
      renderer: Renderer[F, Structure, Reference],
      attacher: Attacher[F, Reference, Target],
      differ: Differ[Structure, Diff],
      patcher: Patcher[F, Target, Diff]
  )(extract: Target => Structure): Schelm[F, View] =
    new DomSchelm(states, structurer, renderer, attacher, differ, patcher)(extract)
}

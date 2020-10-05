package io.taig.schelm.interpreter

import cats.effect.implicits._
import cats.effect.{Concurrent, Resource}
import cats.implicits._
import io.taig.schelm.algebra._
import io.taig.schelm.util.PathTraversal
import io.taig.schelm.util.PathTraversal.ops._

final class DomSchelm[F[_], View, Reference, Target: PathTraversal, Diff](
    states: StateManager[F, View],
    renderer: Renderer[F, View, Reference],
    attacher: Attacher[F, Reference, Target],
    differ: Differ[View, Diff],
    patcher: Patcher[F, Target, Diff]
)(implicit F: Concurrent[F])
    extends Schelm[F, View] {
  override def start(app: View): Resource[F, Unit] =
    for {
      reference1 <- Resource.liftF(renderer.render(app))
      reference2 <- Resource.liftF(attacher.attach(reference1))
      _ <- states.subscription
        .evalScan(reference2) { (reference, update) =>
          reference.modify[F](update.path) { reference =>
            differ.diff(???, update.view)
            ???
          }
          ???
//          reference.update(update.path) { reference =>
//            differ
//              .diff(HtmlReference(reference).html, update.html)
//              .traverse(patcher.patch(HtmlReference(reference), _))
//              .map(_.map(_.reference).getOrElse(reference))
//          }
          F.pure(reference)
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
  def apply[F[_]: Concurrent, View, Reference, Target: PathTraversal, Diff](
      states: StateManager[F, View],
      renderer: Renderer[F, View, Reference],
      attacher: Attacher[F, Reference, Target],
      differ: Differ[View, Diff],
      patcher: Patcher[F, Target, Diff]
  ): Schelm[F, View] =
    new DomSchelm(states, renderer, attacher, differ, patcher)
}

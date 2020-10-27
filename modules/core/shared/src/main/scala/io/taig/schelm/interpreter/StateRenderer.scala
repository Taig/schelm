package io.taig.schelm.interpreter

import cats.data.Kleisli
import cats.effect.kernel.Sync
import cats.implicits._
import io.taig.schelm.algebra.{Renderer, StateManager}
import io.taig.schelm.data._
import io.taig.schelm.util.NodeTraverse
import io.taig.schelm.util.NodeTraverse.ops._

object StateRenderer {
  def apply[F[_], G[_]: NodeTraverse, A](
      renderer: Renderer[F, Fix[G], A],
      states: StateManager[F, A]
  )(implicit F: Sync[F]): Renderer[Kleisli[F, Path, *], Fix[λ[B => State[F, G[B]]]], Fix[G]] = {
    def currentState[B](snapshot: StateTree[_], initial: B, index: Int): F[B] =
      snapshot.values
        .lift(index)
        .traverse(state => F.delay(state.asInstanceOf[B]))
        .map(_.getOrElse(initial))

    def stateful[C](
        state: Stateful[F, C, G[Fix[λ[B => State[F, G[B]]]]]],
        path: Path,
        snapshot: StateTree[_],
        index: Int
    ): F[Fix[G]] = {
      val update = new ((C => C) => F[Unit]) {
        override def apply(f: C => C): F[Unit] = {
          // TODO can the existing snapshot instance be used here or do we have to get the most recent one?
          currentState(snapshot, state.initial, index).flatMap { previous =>
            val current = f(previous)

            render(Fix[λ[B => State[F, G[B]]]](state.render(this, current)), path, snapshot, index + 1)
              .flatMap(renderer.run)
              .flatMap(states.submit(path, _, state.initial, current, index))
          }
        }
      }

      currentState(snapshot, state.initial, index).flatMap { current =>
        render(Fix[λ[B => State[F, G[B]]]](state.render(update, current)), path, snapshot, index + 1)
      }
    }

    def stateless(
        state: Stateless[G[Fix[λ[B => State[F, G[B]]]]]],
        path: Path,
        snapshot: StateTree[_]
    ): F[Fix[G]] =
      state.value
        .traverseWithKey((key, node) =>
          render(node, path / key, snapshot.get(Path.Root / key).getOrElse(StateTree.Empty), index = 0)
        )
        .map(Fix.apply)

    def render(state: Fix[λ[B => State[F, G[B]]]], path: Path, snapshot: StateTree[_], index: Int): F[Fix[G]] =
      state.unfix match {
        case state: Stateful[F, _, G[Fix[λ[B => State[F, G[B]]]]]] => stateful(state, path, snapshot, index)
        case state: Stateless[G[Fix[λ[B => State[F, G[B]]]]]]      => stateless(state, path, snapshot)
      }

    Kleisli { state =>
      Kleisli { path =>
        states.snapshot.map(_.get(path).getOrElse(StateTree.Empty)).flatMap(render(state, path, _, index = 0))
      }
    }
  }

  def root[F[_]: Sync, G[_]: NodeTraverse, A](
      renderer: Renderer[F, Fix[G], A],
      states: StateManager[F, A]
  ): Renderer[F, Fix[λ[B => State[F, G[B]]]], Fix[G]] =
    StateRenderer(renderer, states).mapK(Kleisli.applyK(Path.Root))
}

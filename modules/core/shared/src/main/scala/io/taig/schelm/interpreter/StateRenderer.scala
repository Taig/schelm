package io.taig.schelm.interpreter

import cats.Monad
import cats.arrow.FunctionK
import cats.data.Kleisli
import cats.implicits._
import io.taig.schelm.algebra.{Renderer, StateManager}
import io.taig.schelm.data.{Fix, Path, State}
import io.taig.schelm.util.NodeTraverse
import io.taig.schelm.util.NodeTraverse.ops._

final class StateRenderer[F[_]: Monad, G[_]: NodeTraverse](states: StateManager[F, G[Fix[G]]])
    extends Renderer[Kleisli[F, Path, *], Fix[λ[A => State[F, G[A]]]], Fix[G]] {
  override def render(state: Fix[λ[A => State[F, G[A]]]]): Kleisli[F, Path, Fix[G]] =
    Kleisli { path =>
      state.unfix match {
        case state: State.Stateful[F, _, G[Fix[λ[A => State[F, G[A]]]]]] => stateful(state, path)
        case State.Stateless(value) =>
          NodeTraverse[G].traverseWithKey(value)((key, node) => render(node).run(path / key)).map(Fix.apply)
      }
    }

  def stateful[A](state: State.Stateful[F, A, G[Fix[λ[B => State[F, G[B]]]]]], path: Path): F[Fix[G]] = {
    val get = states.get[A](path).map(_.getOrElse(state.initial))

    val update = new ((A => A) => F[Unit]) {
      override def apply(f: A => A): F[Unit] =
        get.map(f).flatMap { update =>
          state
            .render(this, update)
            .traverseWithKey((key, html) => render(html).run(path / key))
            .flatMap(states.submit(path, state.initial, update, _))
        }
    }

    get.flatMap { current =>
      state
        .render(update, current)
        .traverseWithKey((key, html) => render(html).run(path / key))
        .map(Fix.apply)
    }
  }
}

object StateRenderer {
  def apply[F[_]: Monad, G[_]: NodeTraverse](
      states: StateManager[F, G[Fix[G]]]
  ): Renderer[Kleisli[F, Path, *], Fix[λ[A => State[F, G[A]]]], Fix[G]] =
    new StateRenderer[F, G](states)

  def root[F[_]: Monad, G[_]: NodeTraverse](
      states: StateManager[F, G[Fix[G]]]
  ): Renderer[F, Fix[λ[A => State[F, G[A]]]], Fix[G]] =
    StateRenderer(states).mapK(λ[FunctionK[Kleisli[F, Path, *], F]](_.run(Path.Root)))
}

package io.taig.schelm.interpreter

import cats.Monad
import cats.data.Kleisli
import cats.implicits._
import io.taig.schelm.algebra.{Renderer, StateManager}
import io.taig.schelm.data.{Fix, Path, State}
import io.taig.schelm.util.NodeTraverse
import io.taig.schelm.util.NodeTraverse.ops._

object StateRenderer {
  def apply[F[_]: Monad, G[_]: NodeTraverse, A](
      renderer: Renderer[F, Fix[G], A],
      states: StateManager[F, A]
  ): Renderer[Kleisli[F, Path, *], Fix[λ[B => State[F, G[B]]]], Fix[G]] = {
    def stateful[C](state: State.Stateful[F, C, G[Fix[λ[B => State[F, G[B]]]]]], path: Path): F[Fix[G]] = {
      val get = states.get[C](path).map(_.getOrElse(state.initial))

      val update = new ((C => C) => F[Unit]) {
        override def apply(f: C => C): F[Unit] =
          get.map(f).flatMap { update =>
            state
              .render(this, update)
              .traverseWithKey((key, html) => render(html).run(path / key))
              .flatMap(view => renderer.run(Fix(view)))
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

    def render(state: Fix[λ[B => State[F, G[B]]]]): Kleisli[F, Path, Fix[G]] =
      Kleisli { path =>
        state.unfix match {
          case state: State.Stateful[F, _, G[Fix[λ[B => State[F, G[B]]]]]] => stateful(state, path)
          case State.Stateless(value) =>
            value.traverseWithKey((key, node) => render(node).run(path / key)).map(Fix.apply)
        }
      }

    Kleisli(render)
  }

  def root[F[_]: Monad, G[_]: NodeTraverse, A](
      renderer: Renderer[F, Fix[G], A],
      states: StateManager[F, A]
  ): Renderer[F, Fix[λ[B => State[F, G[B]]]], Fix[G]] =
    StateRenderer(renderer, states).mapK(Kleisli.applyK(Path.Root))
}

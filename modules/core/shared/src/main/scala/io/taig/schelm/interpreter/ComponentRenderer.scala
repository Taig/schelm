package io.taig.schelm.interpreter

import cats.Monad
import cats.data.Kleisli
import cats.implicits._
import io.taig.schelm.algebra.{Renderer, StateManager}
import io.taig.schelm.data._

final class ComponentRenderer[F[_]: Monad](states: StateManager[F])
    extends Renderer[Kleisli[F, Path, *], ComponentHtml[F], Html[F]] {
  override def render(html: ComponentHtml[F]): Kleisli[F, Path, Html[F]] = Kleisli { path =>
    html.state match {
      case state: State.Stateful[F, _, Node[F, Listeners[F], ComponentHtml[F]]] => stateful(state, path)
      case State.Stateless(node) =>
        node.traverseWithKey((key, html) => render(html).run(path / key)).map(Html.apply)
    }
  }

  def stateful[A](state: State.Stateful[F, A, Node[F, Listeners[F], ComponentHtml[F]]], path: Path): F[Html[F]] = {
    val get = states.get[A](path).map(_.getOrElse(state.initial))

    val update = new ((A => A) => F[Unit]) {
      override def apply(f: A => A): F[Unit] =
        get.map(f).flatMap { update =>
          state
            .render(this, update)
            .traverseWithKey((key, html) => render(html).run(path / key))
            .map(Html.apply)
            .flatMap(states.submit(path, state.initial, update, _))
        }
    }

    get.flatMap { current =>
      state
        .render(update, current)
        .traverseWithKey((key, html) => render(html).run(path / key))
        .map(Html.apply)
    }
  }
}

object ComponentRenderer {
  def apply[F[_]: Monad](states: StateManager[F]): Renderer[Kleisli[F, Path, *], ComponentHtml[F], Html[F]] =
    new ComponentRenderer[F](states)
}

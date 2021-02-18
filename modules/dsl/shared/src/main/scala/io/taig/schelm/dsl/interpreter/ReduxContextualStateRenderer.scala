package io.taig.schelm.dsl.interpreter

import scala.annotation.tailrec

import cats.data.Kleisli
import cats.syntax.all._
import cats.{MonadThrow, Traverse}
import io.taig.schelm.algebra.{Renderer, StateManager}
import io.taig.schelm.data._
import io.taig.schelm.redux.algebra.EventManager
import io.taig.schelm.redux.data.Redux

final class ReduxContextualStateRenderer[F[_]: MonadThrow, G[_]: Traverse, Event, Context](
    events: EventManager[F, Event],
    manager: StateManager[F]
) extends Renderer[Kleisli[F, (Context, Tree[Any]), *], Fix[
      λ[a => Redux[F, Event, Contextual[Context, State[F, Namespace[G[a]]]]]]
    ], Fix[G]] {
  override def render(
      redux: Fix[λ[a => Redux[F, Event, Contextual[Context, State[F, Namespace[G[a]]]]]]]
  ): Kleisli[F, (Context, Tree[Any]), Fix[G]] = Kleisli { case (context, states) =>
    renderRedux(redux.unfix, context, states, Identification.Root)
  }

  def renderRedux(
      redux: Redux[F, Event, Contextual[Context, State[F, Namespace[
        G[Fix[λ[a => Redux[F, Event, Contextual[Context, State[F, Namespace[G[a]]]]]]]]
      ]]]],
      context: Context,
      states: Tree[Any],
      identification: Identification
  ): F[Fix[G]] = {
    renderContextual(redux.provide(events), context, states, identification)
  }

  def renderContextual(
      contextual: Contextual[Context, State[F, Namespace[
        G[Fix[λ[a => Redux[F, Event, Contextual[Context, State[F, Namespace[G[a]]]]]]]]
      ]]],
      context: Context,
      states: Tree[Any],
      identification: Identification
  ): F[Fix[G]] = contextual match {
    case contextual: Contextual.Local[
          Context,
          State[F, Namespace[G[Fix[λ[X => Redux[F, Event, Contextual[Context, State[F, Namespace[G[X]]]]]]]]]]
        ] =>
      val local = contextual.context(context)
      renderState(contextual.render(local), local, states, identification)
    case contextual: Contextual.Pure[
          State[F, Namespace[G[Fix[λ[X => Redux[F, Event, Contextual[Context, State[F, Namespace[G[X]]]]]]]]]]
        ] =>
      renderState(contextual.value, context, states, identification)

  }

  def renderState(
      state: State[F, Namespace[G[Fix[λ[a => Redux[F, Event, Contextual[Context, State[F, Namespace[G[a]]]]]]]]]],
      context: Context,
      states: Tree[Any],
      identification: Identification
  ): F[Fix[G]] = state match {
    case state: State.Stateful[F, _, Namespace[
          G[Fix[λ[X => Redux[F, Event, Contextual[Context, State[F, Namespace[G[X]]]]]]]]
        ]] =>
      val next = identification / state.identifier
      val subtree = states.get(state.identifier).orEmpty

      state.internalStateCurrent(subtree).liftTo[F].flatMap { current =>
        renderState(state.render(state.internalStateUpdate(manager, next), current), context, subtree, next)
      }
    case state: State.Stateless[Namespace[
          G[Fix[λ[X => Redux[F, Event, Contextual[Context, State[F, Namespace[G[X]]]]]]]]
        ]] =>
      renderNamespace(state.value, context, states, identification)
  }

  @tailrec
  def renderNamespace(
      namespace: Namespace[G[Fix[λ[a => Redux[F, Event, Contextual[Context, State[F, Namespace[G[a]]]]]]]]],
      context: Context,
      states: Tree[Any],
      identification: Identification
  ): F[Fix[G]] =
    namespace match {
      case Namespace.Anonymous(value) =>
        value.traverse(fix => renderRedux(fix.unfix, context, states, identification)).map(Fix[G])
      case Namespace.Identified(identifier, namespace) =>
        renderNamespace(namespace, context, states.get(identifier).orEmpty, identification / identifier)
    }
}

object ReduxContextualStateRenderer {
  def apply[F[_]: MonadThrow, G[_]: Traverse, Event, Context](
      events: EventManager[F, Event],
      manager: StateManager[F]
  ): Renderer[Kleisli[F, (Context, Tree[Any]), *], Fix[
    λ[a => Redux[F, Event, Contextual[Context, State[F, Namespace[G[a]]]]]]
  ], Fix[G]] =
    new ReduxContextualStateRenderer[F, G, Event, Context](events, manager)
}

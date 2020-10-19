package io.taig.schelm.interpreter

import cats.data.Kleisli
import cats.implicits._
import cats.{Applicative, Functor}
import io.taig.schelm.algebra.Renderer
import io.taig.schelm.data.{Contextual, Fix}

object ContextualRenderer {
  def apply[F[_]: Applicative, G[_]: Functor, Context]
      : Renderer[Kleisli[F, Context, *], Fix[λ[A => Contextual[Context, G[A]]]], Fix[G]] = {
    def render(widget: Fix[λ[A => Contextual[Context, G[A]]]]): Context => Fix[G] = { context =>
      widget.unfix match {
        case Contextual.Patch(f, widget) =>
          // TODO is this right?
          Fix(widget.map(render(_).apply(f(context).asInstanceOf[Context])))
        case Contextual.Pure(value) => Fix(value.map(render(_).apply(context)))
        case Contextual.Render(f)   => Fix(f(context).map(render(_).apply(context)))
      }
    }

    Kleisli(widget => Kleisli(context => render(widget).apply(context).pure[F]))
  }

  def fromContext[F[_]: Applicative, G[_]: Functor, Context](
      context: Context
  ): Renderer[F, Fix[λ[A => Contextual[Context, G[A]]]], Fix[G]] =
    ContextualRenderer[F, G, Context].mapK(Kleisli.applyK(context))
}

package io.taig.schelm.interpreter

import cats.data.Kleisli
import cats.implicits._
import cats.{Applicative, Functor}
import io.taig.schelm.algebra.Renderer
import io.taig.schelm.data.{Fix, Widget}

object WidgetRenderer {
  def apply[F[_]: Applicative, G[_]: Functor, Context]
      : Renderer[Kleisli[F, Context, *], Fix[λ[A => Widget[Context, G[A]]]], Fix[G]] = {
    def render(widget: Fix[λ[A => Widget[Context, G[A]]]]): Context => Fix[G] = { context =>
      widget.unfix match {
        case widget: Widget.Patch[Context, F[Fix[λ[A => Widget[Context, G[A]]]]]] =>
          Fix(widget.widget.map(render(_).apply(widget.f(context))))
        case Widget.Pure(value) => Fix(value.map(render(_).apply(context)))
        case Widget.Render(f)   => Fix(f(context).map(render(_).apply(context)))
      }
    }

    Kleisli(widget => Kleisli(context => render(widget).apply(context).pure[F]))
  }

  def default[F[_]: Applicative, G[_]: Functor, Context](
      context: Context
  ): Renderer[F, Fix[λ[A => Widget[Context, G[A]]]], Fix[G]] =
    WidgetRenderer[F, G, Context].mapK(Kleisli.applyK(context))
}

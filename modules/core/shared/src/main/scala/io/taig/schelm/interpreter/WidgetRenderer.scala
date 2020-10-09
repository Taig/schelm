package io.taig.schelm.interpreter

import cats.data.Kleisli
import cats.implicits._
import cats.{~>, Applicative, Functor, Id}
import io.taig.schelm.algebra.Renderer
import io.taig.schelm.data.{Fix, Widget}
import io.taig.schelm.util.FunctionKs

final class WidgetRenderer[F[_]: Functor, Context]
    extends Renderer[Kleisli[Id, Context, *], Fix[λ[A => Widget[Context, F[A]]]], Fix[F]] {
  override def render(widget: Fix[λ[A => Widget[Context, F[A]]]]): Kleisli[Id, Context, Fix[F]] = Kleisli { context =>
    widget.unfix match {
      case widget: Widget.Patch[Context, F[Fix[λ[A => Widget[Context, F[A]]]]]] =>
        Fix(widget.widget.map(render(_).run(widget.f(context))))
      case Widget.Pure(value) => Fix(value.map(render(_).run(context)))
      case Widget.Render(f)   => Fix(f(context).map(render(_).run(context)))
    }
  }
}

object WidgetRenderer {
  def apply[F[_]: Applicative, G[_]: Functor, Context]
      : Renderer[Kleisli[F, Context, *], Fix[λ[A => Widget[Context, G[A]]]], Fix[G]] =
    new WidgetRenderer[G, Context].mapK(new (Kleisli[Id, Context, *] ~> Kleisli[F, Context, *]) {
      override def apply[A](fa: Kleisli[Id, Context, A]): Kleisli[F, Context, A] = fa.mapK(FunctionKs.liftId[F])
    })

  def default[F[_]: Applicative, G[_]: Functor, Context](
      context: Context
  ): Renderer[F, Fix[λ[A => Widget[Context, G[A]]]], Fix[G]] =
    WidgetRenderer[F, G, Context].mapK(Kleisli.applyK(context))
}

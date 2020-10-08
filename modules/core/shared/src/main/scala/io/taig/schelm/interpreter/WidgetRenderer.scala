package io.taig.schelm.interpreter

import cats.arrow.FunctionK
import cats.data.Kleisli
import cats.implicits._
import cats.{Applicative, Traverse}
import io.taig.schelm.algebra.Renderer
import io.taig.schelm.data.{Fix, Widget}

final class WidgetRenderer[F[_]: Applicative, G[_]: Traverse, Context]
    extends Renderer[Kleisli[F, Context, *], Widget.⟳[Context, G], G[Fix[G]]] {
  override def render(widget: Widget.⟳[Context, G]): Kleisli[F, Context, G[Fix[G]]] = Kleisli { context =>
    widget match {
      case widget: Widget.Patch[Context, Fix[λ[A => G[Widget[Context, A]]]]] =>
        render(widget.widget).run(widget.f(context))
      case Widget.Pure(value) => value.unfix.traverse(render(_).run(context).map(Fix.apply))
      case Widget.Render(f)   => render(f(context)).run(context)
    }
  }
}

object WidgetRenderer {
  def apply[F[_]: Applicative, G[_]: Traverse, Context]
      : Renderer[Kleisli[F, Context, *], Widget.⟳[Context, G], G[Fix[G]]] =
    new WidgetRenderer[F, G, Context]

  def default[F[_]: Applicative, G[_]: Traverse, Context](
      context: Context
  ): Renderer[F, Widget.⟳[Context, G], G[Fix[G]]] =
    WidgetRenderer[F, G, Context].mapK(λ[FunctionK[Kleisli[F, Context, *], F]](_.run(context)))
}

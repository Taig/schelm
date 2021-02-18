package io.taig.schelm.dsl.interpreter

import cats.data.Kleisli
import cats.syntax.all._
import cats.{Monad, MonadThrow, Traverse}
import io.taig.schelm.algebra.{Renderer, StateManager}
import io.taig.schelm.css.data.{Css, CssHtml, Stylesheet}
import io.taig.schelm.css.interpreter.CssHtmlRenderer
import io.taig.schelm.data._
import io.taig.schelm.dsl.Widget
import io.taig.schelm.redux.algebra.EventManager
import io.taig.schelm.redux.data.Redux

final class WidgetRenderer[F[_]: Monad, Event, Context](
    contextual: Renderer[
      Kleisli[F, (Context, Tree[Any]), *],
      Fix[λ[a => Redux[F, Event, Contextual[Context, State[F, Namespace[Css[Node[F, a]]]]]]]],
      Fix[λ[a => Css[Node[F, a]]]]
    ],
    html: Renderer[F, CssHtml[F], (Stylesheet, Html[F])]
) extends Renderer[Kleisli[F, (Context, Tree[Any]), *], Widget[F, Event, Context], (Stylesheet, Html[F])] {
  override def render(widget: Widget[F, Event, Context]): Kleisli[F, (Context, Tree[Any]), (Stylesheet, Html[F])] =
    Kleisli { input =>
      contextual.render(widget.toFix).run(input).flatMap(html.render)
    }
}

object WidgetRenderer {
  def default[F[_]: MonadThrow, Event, Context](
      events: EventManager[F, Event],
      manager: StateManager[F]
  ): Renderer[Kleisli[F, (Context, Tree[Any]), *], Widget[F, Event, Context], (Stylesheet, Html[F])] = {
    implicit val traverse: Traverse[λ[a => Css[Node[F, a]]]] = Traverse[Css].compose[Node[F, *]]
    val contextual = ReduxContextualStateRenderer[F, λ[a => Css[Node[F, a]]], Event, Context](events, manager)
    val html = CssHtmlRenderer[F]
    new WidgetRenderer(contextual, html)
  }
}

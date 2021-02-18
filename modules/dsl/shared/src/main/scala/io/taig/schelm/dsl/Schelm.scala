package io.taig.schelm.dsl

import cats.effect.Async
import cats.effect.kernel.Resource
import cats.syntax.all._
import io.taig.schelm.algebra.Dom
import io.taig.schelm.css.interpreter.StylesheetAttacher
import io.taig.schelm.data.Tree
import io.taig.schelm.dsl.interpreter.WidgetRenderer
import io.taig.schelm.interpreter.{HtmlRenderer, NodeAttacher, QueueStateManager}
import io.taig.schelm.redux.algebra.EventManager

object Schelm {
  def start[F[_]: Async, Event, Context](dom: Dom[F])(
      parent: Dom.Element,
      context: Context,
      widget: Widget[F, Event, Context]
  ): Resource[F, Unit] = Resource.eval(QueueStateManager.unbounded[F]).evalMap { states =>
    val events = EventManager.noop[F, Event]
    val renderer = WidgetRenderer.default[F, Event, Context](events, states)

    renderer
      .render(widget)
      .run((context, Tree.Empty))
      .flatMap { case (stylesheet, html) =>
        for {
          style <- dom.createElement("style")
          head <- dom.head.flatMap(_.liftTo[F](new IllegalStateException()))
          _ <- dom.appendChild(head, style)
          _ <- StylesheetAttacher.default(dom)(style).attach(stylesheet)
          reference <- HtmlRenderer(dom).render(html)
          _ <- NodeAttacher(dom)(parent).attach(reference.nodes)
        } yield ()
      }
  }
}

package io.taig.schelm.dsl.interpreter

import cats.implicits._
import cats.{Applicative, Id}
import io.taig.schelm.algebra.Renderer
import io.taig.schelm.css.data.WidgetStateCssHtml
import io.taig.schelm.dsl.data.DslWidget
import io.taig.schelm.util.FunctionKs

final class DslWidgetRenderer[F[_], Context]
    extends Renderer[Id, DslWidget[F, Context], WidgetStateCssHtml[F, Context]] {
  override def render(dsl: DslWidget[F, Context]): WidgetStateCssHtml[F, Context] = dsl match {
    case DslWidget.Pure(widget)                     => WidgetStateCssHtml(widget.map(_.map(_.map(_.map(render)))))
    case component: DslWidget.Component[F, Context] => render(component.render)
  }
}

object DslWidgetRenderer {
  def apply[F[_], Context](
      implicit F: Applicative[F]
  ): Renderer[F, DslWidget[F, Context], WidgetStateCssHtml[F, Context]] =
    new DslWidgetRenderer[F, Context].mapK(FunctionKs.liftId)
}

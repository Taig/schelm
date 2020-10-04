package io.taig.schelm.dsl.interpreter

import cats.Id
import cats.implicits._
import io.taig.schelm.algebra.Renderer
import io.taig.schelm.css.data.CssStateWidget
import io.taig.schelm.dsl.data.DslWidget

final class DslWidgetRenderer[F[_], Context] extends Renderer[Id, DslWidget[F, Context], CssStateWidget[F, Context]] {
  override def render(dsl: DslWidget[F, Context]): CssStateWidget[F, Context] = dsl match {
    case DslWidget.Pure(widget)                     => CssStateWidget(widget.map(_.map(_.map(_.map(render)))))
    case component: DslWidget.Component[F, Context] => render(component.render)
  }
}

package io.taig.schelm.dsl.operation

import cats.syntax.all._
import io.taig.schelm.css.data.{Declaration, Style}
import io.taig.schelm.dsl.Widget

final class WidgetCssOperations[F[_], Event, Context](val widget: Widget[F, Event, Context]) extends AnyVal {
  def apply(declarations: Declaration*): Widget[F, Event, Context] = modify(style => style ++ Style.from(declarations))

  def set(style: Style): Widget[F, Event, Context] = modify(_ => style)

  def modify(f: Style => Style): Widget[F, Event, Context] = ???
//    Widget(widget.unfix.map(_.map(_.map(_.map(css => css.copy(style = f(css.style)))))))
}

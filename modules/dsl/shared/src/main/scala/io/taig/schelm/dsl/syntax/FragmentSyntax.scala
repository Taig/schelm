package io.taig.schelm.dsl.syntax

import cats.implicits._
import io.taig.schelm.css.data.StylesheetWidget
import io.taig.schelm.data.{Children, Fragment}
import io.taig.schelm.dsl.DslWidget
import io.taig.schelm.dsl.operation.ChildrenOperation

final class FragmentSyntax[Event, Context](widget: DslWidget[Fragment[+*], Event, Context])
    extends ChildrenSyntax[Fragment[+*], Event, Context] {
  def children: ChildrenOperation[Fragment[+*], Event, Context] =
    new ChildrenOperation[Fragment[+*], Event, Context] {
      override def patch(
          f: Children[StylesheetWidget[Event, Context]] => Children[StylesheetWidget[Event, Context]]
      ): DslWidget[Fragment, Event, Context] =
        widget.map(_.map(element => element.copy(children = f(element.children))))
    }
}

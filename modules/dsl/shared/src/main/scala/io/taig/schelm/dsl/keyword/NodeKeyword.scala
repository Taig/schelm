package io.taig.schelm.dsl.keyword

import io.taig.schelm.css.data.{Css, Style}
import io.taig.schelm.data._
import io.taig.schelm.dsl.builder.{ElementNormalBuilder, ElementVoidBuilder}
import io.taig.schelm.dsl.data.DslNode
import io.taig.schelm.redux.data.Redux

trait NodeKeyword extends ElementKeyword {
  def element(name: String): ElementNormalBuilder = new ElementNormalBuilder(name)

  final def void(name: String): ElementVoidBuilder = new ElementVoidBuilder(name)

  final def fragment[F[_], Event, Context](
      children: Children[DslNode[F, Event, Context]] = Children.Empty
  ): DslNode[F, Event, Context] =
    DslNode.Pure(Redux.Pure(Widget.Pure(State.Stateless(Css(Node.Fragment(children), Style.Empty)))))

  final def text(value: String): DslNode[Nothing, Nothing, Any] =
    DslNode.Pure(
      Redux.Pure(Widget.Pure(State.Stateless(Css(Node.Text(value, Listeners.Empty, Lifecycle.Noop), Style.Empty))))
    )
}

object NodeKeyword extends NodeKeyword

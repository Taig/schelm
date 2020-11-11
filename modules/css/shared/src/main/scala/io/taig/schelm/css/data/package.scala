package io.taig.schelm.css

import cats.Applicative
import cats.implicits._
import io.taig.schelm.data._
import io.taig.schelm.util.PathModification
import org.scalajs.dom.raw.Event

package object data {
  type CssHtml[F[_]] = Fix[λ[A => Css[Node[F, Listeners[F], A]]]]

  object CssHtml {
    @inline
    def apply[F[_]](css: Css[Node[F, Listeners[F], CssHtml[F]]]): CssHtml[F] =
      Fix[λ[A => Css[Node[F, Listeners[F], A]]]](css)

    implicit val traversal: PathModification[CssHtml] = new PathModification[CssHtml] {
      override def children[G[_]](value: CssHtml[G]): Option[Children[CssHtml[G]]] = ???

      override def modifyChildren[G[_]: Applicative](value: CssHtml[G])(
          f: Children[CssHtml[G]] => G[Children[CssHtml[G]]]
      ): G[CssHtml[G]] = ???

      override def toHtml[G[_]](css: CssHtml[G]): Html[G] = Html(css.unfix.value.map(toHtml))
    }
  }

  type StateCssHtml[F[_]] = Fix[λ[A => State[F, Css[Node[F, Listeners[F], A]]]]]

  object StateCssHtml {
    @inline
    def apply[F[_]](state: State[F, Css[Node[F, Listeners[F], StateCssHtml[F]]]]): StateCssHtml[F] =
      Fix[λ[A => State[F, Css[Node[F, Listeners[F], A]]]]](state)

    implicit val traversal: PathModification[StateCssHtml] = new PathModification[StateCssHtml] {
      override def children[G[_]](value: StateCssHtml[G]): Option[Children[StateCssHtml[G]]] = ???

      override def modifyChildren[G[_]: Applicative](value: StateCssHtml[G])(
          f: Children[StateCssHtml[G]] => G[Children[StateCssHtml[G]]]
      ): G[StateCssHtml[G]] = ???

      override def toHtml[G[_]](state: StateCssHtml[G]): Html[G] = ???
    }
  }

  type WidgetCssHtml[F[_], Context] = Fix[λ[A => Contextual[Context, Css[Node[F, Listeners[F], A]]]]]

  object WidgetCssHtml {
    @inline
    def apply[F[_], Context](
        widget: Contextual[Context, Css[Node[F, Listeners[F], WidgetCssHtml[F, Context]]]]
    ): WidgetCssHtml[F, Context] =
      Fix[λ[A => Contextual[Context, Css[Node[F, Listeners[F], A]]]]](widget)
  }

  type WidgetStateCssHtml[F[_], Context] =
    Fix[λ[A => Contextual[Context, State[F, Css[Node[F, Listeners[F], A]]]]]]

  object WidgetStateCssHtml {
    @inline
    def apply[F[_], Context](
        widget: Contextual[Context, State[F, Css[Node[F, Listeners[F], WidgetStateCssHtml[F, Context]]]]]
    ): WidgetStateCssHtml[F, Context] =
      Fix[λ[A => Contextual[Context, State[F, Css[Node[F, Listeners[F], A]]]]]](widget)
  }
}

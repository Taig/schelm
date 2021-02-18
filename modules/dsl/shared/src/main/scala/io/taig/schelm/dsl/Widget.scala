package io.taig.schelm.dsl

import cats.syntax.all._
import io.taig.schelm.css.data.Css
import io.taig.schelm.data._
import io.taig.schelm.dsl.operation.{WidgetAttributesOperations, WidgetChildrenOperations, WidgetCssOperations}
import io.taig.schelm.redux.data.Redux

sealed abstract class Widget[+F[_], +Event, -Context] extends Product with Serializable {
  def toFix[F1[a] >: F[a], E >: Event, B <: Context]
      : Fix[λ[a => Redux[F1, E, Contextual[B, State[F1, Namespace[Css[Node[F1, a]]]]]]]]

  def unfix: Redux[F, Event, Contextual[Context, State[F, Namespace[Css[Node[F, Widget[F, Event, Context]]]]]]]
}

object Widget {
  final case class Element[F[_], Event, Context](
      unfix: Redux[F, Event, Contextual[Context, State[F, Namespace[Css[Node.Element[F, Widget[F, Event, Context]]]]]]]
  ) extends Widget[F, Event, Context] {
    override def toFix[F1[a] >: F[a], E >: Event, B <: Context]
        : Fix[λ[a => Redux[F1, E, Contextual[B, State[F1, Namespace[Css[Node[F1, a]]]]]]]] =
      Fix[λ[a => Redux[F1, E, Contextual[B, State[F1, Namespace[Css[Node[F1, a]]]]]]]](
        unfix.map(_.map(_.map(_.map(_.map(_.map(_.toFix))))))
      )
  }

  final case class Fragment[F[_], Event, Context](
      unfix: Redux[F, Event, Contextual[Context, State[F, Namespace[Css[Node.Fragment[F, Widget[F, Event, Context]]]]]]]
  ) extends Widget[F, Event, Context] {
    override def toFix[F1[a] >: F[a], E >: Event, B <: Context]
        : Fix[λ[a => Redux[F1, E, Contextual[B, State[F1, Namespace[Css[Node[F1, a]]]]]]]] =
      Fix[λ[a => Redux[F1, E, Contextual[B, State[F1, Namespace[Css[Node[F1, a]]]]]]]](
        unfix.map(_.map(_.map(_.map(_.map(_.map(_.toFix))))))
      )
  }

  final case class Text[F[_], Event, Context](
      unfix: Redux[F, Event, Contextual[Context, State[F, Namespace[Css[Node.Text[F]]]]]]
  ) extends Widget[F, Event, Context] {
    override def toFix[F1[a] >: F[a], E >: Event, B <: Context]
        : Fix[λ[a => Redux[F1, E, Contextual[B, State[F1, Namespace[Css[Node[F1, a]]]]]]]] =
      Fix[λ[a => Redux[F1, E, Contextual[B, State[F1, Namespace[Css[Node[F1, a]]]]]]]](unfix)
  }

  final case class Window[F[_], Event, Context](
      unfix: Redux[
        F,
        Event,
        Contextual[Context, State[F, Namespace[Css[Node.Environment[F, Widget[F, Event, Context]]]]]]
      ]
  ) extends Widget[F, Event, Context] {
    override def toFix[F1[a] >: F[a], E >: Event, B <: Context]
        : Fix[λ[a => Redux[F1, E, Contextual[B, State[F1, Namespace[Css[Node[F1, a]]]]]]]] =
      Fix[λ[a => Redux[F1, E, Contextual[B, State[F1, Namespace[Css[Node[F1, a]]]]]]]](unfix)
  }
}

//  def attributes[F1[a] >: F[a], E >: Event, C <: Context]: WidgetAttributesOperations[F1, E, C] =
//    new WidgetAttributesOperations(this)
//
//  def css[F1[a] >: F[a], E >: Event, C <: Context]: WidgetCssOperations[F1, E, C] =
//    new WidgetCssOperations(this)
//
//  def children[F1[a] >: F[a], E >: Event, C <: Context]: WidgetChildrenOperations[F1, E, C] =
//    new WidgetChildrenOperations(this)
//
//  def provide(context: Context): Widget[F, Event, Any] = Widget(unfix.map {
//    case contextual: Contextual.Local[Context, State[F, Namespace[Css[Node[F, Widget[F, Event, Context]]]]]] =>
//      val local = contextual.context(context)
//      Contextual.Pure(contextual.render(local).map(_.map(_.map(_.map(_.provide(local))))))
//    case Contextual.Pure(value) =>
//      Contextual.Pure(value.map(_.map(_.map(_.map(_.provide(context))))))
//  })
//
//  def select[B](f: B => Context): Widget[F, Event, B] = contextual[B](value => provide(f(value)))
//}

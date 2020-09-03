package io.taig.schelm.css.data

import io.taig.schelm.data.{Element, Html, Node}
import cats.implicits._

sealed abstract class StylesheetHtml[+F[+_], +Event] extends Product with Serializable

object StylesheetHtml {
  final case class Styled[F[+A] <: Element[Event, A], +Event](element: F[StylesheetHtml[Node[Event, +*], Event]], styles: Stylesheet)
      extends StylesheetHtml[F, Event]

  final case class Unstyled[F[+_], +Event](node: F[StylesheetHtml[Node[Event, +*], Event]]) extends StylesheetHtml[F, Event]

  def toHtml[Event](html: StylesheetHtml[Node[Event, +*], Event]): (Html[Event], Stylesheet) =
    html match {
      case Styled(element @ Element.Normal(_, _), styles) =>
        val styled = element.children.map(toHtml[Event])
        val stylesheet = styled.foldl(styles) { case (left, (_, right)) => left |+| right }
        val html = Html(element.copy(children = styled.map { case (html, _) => html }))
        (html, stylesheet)
      case Styled(element @ Element.Void(_), styles) => (Html(element), styles)
      case unstyled: Unstyled[Node[Event, +*], Event] =>
        val styled = unstyled.node.map(toHtml[Event])
        val stylesheet = styled.foldl(Stylesheet.Empty) { case (left, (_, right)) => left |+| right }
        val html = Html(styled.map { case (html, _) => html })
        (html, stylesheet)
    }

  //  def fromStyledWidget[Event, Context](widget: StyledWidget[Event, Context], context: Context): StyledHtml2[Event] =
  //    widget.widget match {
  //      case widget: Widget.Patch[Context, StyledNode[Event, StyledWidget[Event, Context]]] =>
  //        fromStyledWidget(StyledWidget(widget.widget), widget.f(context))
  //      case widget: Widget.Pure[StyledNode[Event, StyledWidget[Event, Context]]] =>
  //        widget.node match {
  //          case node: StyledNode.Styled[Event, StyledWidget[Event, Context]] =>
  //            StyledHtml(StyledNode.Styled(node.element.map(fromStyledWidget(_, context)), node.styles))
  //          case node: StyledNode.Unstyled[Event, StyledWidget[Event, Context]] =>
  //            StyledHtml(StyledNode.Unstyled(node.node.map(fromStyledWidget(_, context))))
  //        }
  //      case widget: Widget.Render[Context, StyledNode[Event, StyledWidget[Event, Context]]] =>
  //        fromStyledWidget(StyledWidget(widget.f(context)), context)
  //    }
}

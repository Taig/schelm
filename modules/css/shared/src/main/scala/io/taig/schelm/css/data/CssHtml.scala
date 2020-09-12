package io.taig.schelm.css.data

import cats.implicits._
import io.taig.schelm.Navigator
import io.taig.schelm.data.{Attribute, Attributes, Component, Html}

final case class CssHtml[F[_]](node: CssNode[Component[F, CssHtml[F]]]) extends AnyVal

object CssHtml {
  private val EmptyStyles: Map[Selector, Style] = Map.empty

  def toHtml[F[_]](css: CssHtml[F]): (Html[F], Map[Selector, Style]) = {
    val nodes = css.node.map(_.map(toHtml[F]))
    val navigator: Navigator[Component.Element[F, (Html[F], Map[Selector, Style])], (Html[F], Map[Selector, Style])] =
      ??? // Navigator[Component.Element[F, (Html[F], Map[Selector, Style])], (Html[F], Map[Selector, Style])]

//    val (node, rules) = nodes match {
//      case css: CssNode.Styled[F, (Html[F], Map[Selector, Style])] if !css.style.isEmpty =>
//        val identifier = Identifier(css.style.hashCode)
//        val selector = identifier.toSelector
//        val attribute = Attribute(Attribute.Key.Class, Attribute.Value(identifier.toClass))
//        val update = navigator.attributes(css.element, Attributes.of(attribute) ++ _)
//        (update, Map(selector -> css.style))
//      case css: CssNode.Styled[F, (Html[F], Map[Selector, Style])] => (css.element, EmptyStyles)
//      case css: CssNode.Unstyled[F, (Html[F], Map[Selector, Style])]     => (css.component, EmptyStyles)
//    }
//
//    val html = Html(node.map { case (html, _) => html })
//    val styles = nodes.foldl(rules) { case (left, (_, right)) => left ++ right }
//
//    (html, styles)

    ???
  }

//  implicit def navigator[Event]: CssNavigator[Event, CssHtml[Event], CssHtml[Event]] =
//    new CssNavigator[Event, CssHtml[Event], CssHtml[Event]] {
//      override def attributes(css: CssHtml[Event], f: Attributes => Attributes): CssHtml[Event] =
//        CssHtml(Navigator[Event, CssNode[Event, CssHtml[Event]], CssHtml[Event]].attributes(css.node, f))
//
//      override def listeners(css: CssHtml[Event], f: Listeners[Event] => Listeners[Event]): CssHtml[Event] =
//        CssHtml(Navigator[Event, CssNode[Event, CssHtml[Event]], CssHtml[Event]].listeners(css.node, f))
//
//      override def children(
//          css: CssHtml[Event],
//          f: Children[CssHtml[Event]] => Children[CssHtml[Event]]
//      ): CssHtml[Event] =
//        CssHtml(Navigator[Event, CssNode[Event, CssHtml[Event]], CssHtml[Event]].children(css.node, f))
//
//      override def style(css: CssHtml[Event], f: Style => Style): CssHtml[Event] =
//        CssHtml(CssNavigator[Event, CssNode[Event, CssHtml[Event]], CssHtml[Event]].style(css.node, f))
//    }
}

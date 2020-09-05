package io.taig.schelm.css.data

import cats.implicits._
import io.taig.schelm.Navigator
import io.taig.schelm.css.CssNavigator
import io.taig.schelm.data.{Attributes, Children, Html, Listeners}

final case class CssHtml[+Event](node: CssNode[Event, CssHtml[Event]]) extends AnyVal

object CssHtml {
  private val EmptyStyles: Map[Selector, Style] = Map.empty

  def toHtml[Event](css: CssHtml[Event]): (Html[Event], Map[Selector, Style]) = {
    val nodes = css.node.map(toHtml[Event])

    val html = nodes match {
      case CssNode.Styled(element, _) => Html(element.map { case (html, _) => html })
      case CssNode.Unstyled(node)     => Html(node.map { case (html, _)    => html })
    }

    val rules = css.node match {
      case CssNode.Styled(_, style) if !style.isEmpty =>
        val identifier = Identifier(style.hashCode)
        val selector = identifier.toSelector
        Map(selector -> style)
      case CssNode.Styled(_, _) | CssNode.Unstyled(_) => EmptyStyles
    }

    val styles = nodes.foldl(rules) { case (left, (_, right)) => left ++ right }

    (html, styles)
  }

  implicit def navigator[Event]: CssNavigator[Event, CssHtml[Event], CssHtml[Event]] =
    new CssNavigator[Event, CssHtml[Event], CssHtml[Event]] {
      override def attributes(css: CssHtml[Event], f: Attributes => Attributes): CssHtml[Event] =
        CssHtml(Navigator[Event, CssNode[Event, CssHtml[Event]], CssHtml[Event]].attributes(css.node, f))

      override def listeners(css: CssHtml[Event], f: Listeners[Event] => Listeners[Event]): CssHtml[Event] =
        CssHtml(Navigator[Event, CssNode[Event, CssHtml[Event]], CssHtml[Event]].listeners(css.node, f))

      override def children(
          css: CssHtml[Event],
          f: Children[CssHtml[Event]] => Children[CssHtml[Event]]
      ): CssHtml[Event] =
        CssHtml(Navigator[Event, CssNode[Event, CssHtml[Event]], CssHtml[Event]].children(css.node, f))

      override def style(css: CssHtml[Event], f: Style => Style): CssHtml[Event] =
        CssHtml(CssNavigator[Event, CssNode[Event, CssHtml[Event]], CssHtml[Event]].style(css.node, f))
    }
}

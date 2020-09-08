package io.taig.schelm.interpreter

import cats.Monad
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Renderer}
import io.taig.schelm.data._

final class HtmlRenderer[F[_]: Monad, Event](dom: Dom[F]) extends Renderer[F, Html[Event], HtmlReference[Event]] {
  override def render(html: Html[Event]): F[HtmlReference[Event]] = render(html, parent = None)

  def render(html: Html[Event], parent: Option[Dom.Element]): F[HtmlReference[Event]] =
    html.node match {
      case Node.Element(tag, Node.Element.Type.Void) =>
        // TODO attach listeners
        for {
          node <- dom.createElement(tag.name)
          _ <- tag.attributes.toList.traverse_ {
            case Attribute(key, value) => dom.setAttribute(node, key.value, value.value)
          }
        } yield HtmlReference(Reference.Element(Node.Element(tag, Node.Element.Type.Void), node))
      case Node.Element(tag, Node.Element.Type.Normal(children)) =>
        // TODO attach listeners
        for {
          node <- dom.createElement(tag.name)
          _ <- tag.attributes.toList.traverse_ {
            case Attribute(key, value) => dom.setAttribute(node, key.value, value.value)
          }
          children <- children.traverse(render).map(_.map(_.reference))
          _ <- children.indexed.flatMap(_.toNodes).traverse_(dom.appendChild(node, _))
        } yield HtmlReference(Reference.Element(Node.Element(tag, Node.Element.Type.Normal(children)), node))

      case Node.Fragment(children) =>
        for {
          children <- children.traverse(render).map(_.map(_.reference))
        } yield HtmlReference(Reference.Fragment(Node.Fragment(children)))
      case text @ Node.Text(value, _) =>
        // TODO attach listeners
        dom.createTextNode(value).map(node => HtmlReference(Reference.Text(text, node)))
    }
}
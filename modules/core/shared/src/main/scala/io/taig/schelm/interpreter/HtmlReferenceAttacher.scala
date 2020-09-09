package io.taig.schelm.interpreter

import cats.Applicative
import io.taig.schelm.algebra.{Attacher, Dom}
import io.taig.schelm.data.HtmlReference

object HtmlReferenceAttacher {
  def apply[F[+_], Event, Node, Element <: Node, Text <: Node](
      attacher: Attacher[F, List[Node], Element]
  ): Attacher[F, HtmlReference[Event, Node, Element, Text], Element] =
    new Attacher[F, HtmlReference[Event, Node, Element, Text], Element] {
      override def attach(html: HtmlReference[Event, Node, Element, Text]): F[Element] = attacher.attach(html.toNodes)
    }

  def default[F[+_]: Applicative, Event](
      dom: Dom[F]
  )(parent: dom.Element): Attacher[F, HtmlReference[Event, dom.Node, dom.Element, dom.Text], dom.Element] =
    HtmlReferenceAttacher(NodeAttacher(dom)(parent))
}

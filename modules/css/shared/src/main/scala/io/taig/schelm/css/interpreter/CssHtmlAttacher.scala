package io.taig.schelm.css.interpreter

import cats.Applicative
import cats.implicits._
import io.taig.schelm.algebra.{Attacher, Dom}
import io.taig.schelm.interpreter.HtmlAttacher

final class CssHtmlAttacher[F[_]: Applicative, Event, Node, Element](
    html: Attacher[F, List[Node]],
    css: Attacher[F, String]
) extends Attacher[F, (List[Node], String)] {
  override def attach(structure: (List[Node], String)): F[Unit] = {
    val (nodes, styles) = structure
    css.attach(styles) *> html.attach(nodes)
  }
}

object CssHtmlAttacher {
  def apply[F[_]: Applicative, Event, Node](
      html: Attacher[F, List[Node]],
      css: Attacher[F, String]
  ): Attacher[F, (List[Node], String)] =
    new CssHtmlAttacher(html, css)

  def default[F[_]: Applicative, Event, Node, Element](
      main: Element,
      style: Element,
      dom: Dom.Aux[F, _, Node, Element, _]
  ): Attacher[F, (List[Node], String)] =
    CssHtmlAttacher(HtmlAttacher(dom, main), CssAttacher(dom, style))
}

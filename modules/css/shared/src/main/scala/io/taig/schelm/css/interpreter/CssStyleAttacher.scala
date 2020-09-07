package io.taig.schelm.css.interpreter

import cats.{Functor, Monad}
import cats.implicits._
import io.taig.schelm.algebra.{Attacher, Dom}
import io.taig.schelm.css.data.{Selector, Style}

/** Attach styles to a given `<style>` tag */
final class CssStyleAttacher[F[_]: Functor, Element](dom: Dom.Aux[F, _, _, Element, _], parent: Element)
    extends Attacher[F, Map[Selector, Style], Element] {
  override def attach(styles: Map[Selector, Style]): F[Element] = {
    val stylesheet = CssRenderer.render(styles)
    val text = CssPrinter.print(stylesheet, pretty = true)
    dom.innerHtml(parent, text).as(parent)
  }
}

object CssStyleAttacher {
  def apply[F[_]: Functor, Element](
      dom: Dom.Aux[F, _, _, Element, _],
      parent: Element
  ): Attacher[F, Map[Selector, Style], Element] =
    new CssStyleAttacher(dom, parent)

  /** Create a `<style>` tag in the document's `<head>` and attach the styles to it */
  def auto[F[_]: Monad, Node, Element <: Node](
      dom: Dom.Aux[F, _, Node, Element, _]
  ): F[Attacher[F, Map[Selector, Style], Element]] =
    for {
      style <- dom.createElement("style")
      head <- dom.head
      _ <- dom.appendChild(head, style)
    } yield CssStyleAttacher(dom, style)
}

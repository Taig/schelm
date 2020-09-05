package io.taig.schelm.css.interpreter

import io.taig.schelm.algebra.{Attacher, Dom}
import io.taig.schelm.css.data.{Selector, Style}

final class CssAttacher[F[_], Element](dom: Dom.Aux[F, _, _, Element, _], root: Element)
    extends Attacher[F, Map[Selector, Style]] {
  override def attach(styles: Map[Selector, Style]): F[Unit] = {
    val stylesheet = CssRenderer.render(styles)
    val text = CssPrinter.print(stylesheet, pretty = true)
    dom.innerHtml(root, text)
  }
}

object CssAttacher {
  def apply[F[_], Element](dom: Dom.Aux[F, _, _, Element, _], root: Element): Attacher[F, Map[Selector, Style]] =
    new CssAttacher(dom, root)
}

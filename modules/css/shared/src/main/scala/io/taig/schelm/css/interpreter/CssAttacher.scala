package io.taig.schelm.css.interpreter

import io.taig.schelm.algebra.{Attacher, Dom}

final class CssAttacher[F[_], Element](dom: Dom.Aux[F, _, _, Element, _], root: Element) extends Attacher[F, String] {
  override def attach(structure: String): F[Unit] = dom.innerHtml(root, structure)
}

object CssAttacher {
  def apply[F[_], Element](dom: Dom.Aux[F, _, _, Element, _], root: Element): Attacher[F, String] =
    new CssAttacher(dom, root)
}

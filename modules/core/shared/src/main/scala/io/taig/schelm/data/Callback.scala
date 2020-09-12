package io.taig.schelm.data

import io.taig.schelm.algebra.Dom

object Callback {
  abstract class Element[+F[_]] {
    def apply(dom: Dom)(reference: dom.Element): F[Unit]
  }

  abstract class Fragment[+F[_]] {
    def apply(dom: Dom)(reference: List[dom.Node]): F[Unit]
  }

  abstract class Text[+F[_]] {
    def apply(dom: Dom)(reference: dom.Text): F[Unit]
  }
}

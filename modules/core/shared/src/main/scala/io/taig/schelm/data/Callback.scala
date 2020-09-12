package io.taig.schelm.data

import io.taig.schelm.algebra.Dom

object Callback {
  abstract class Element[+F[_]] {
    def apply(element: Dom.Element): F[Unit]
  }

  abstract class Fragment[+F[_]] {
    def apply(nodes: List[Dom.Node]): F[Unit]
  }

  abstract class Text[+F[_]] {
    def apply(text: Dom.Text): F[Unit]
  }
}

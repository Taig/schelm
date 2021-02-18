package io.taig.schelm.css

import io.taig.schelm.data.{Fix, Node}

package object data {
  type CssHtml[F[_]] = Fix[λ[a => Css[Node[F, a]]]]

  object CssHtml {
    def apply[F[_]](css: Css[Node[F, CssHtml[F]]]): CssHtml[F] = Fix[λ[a => Css[Node[F, a]]]](css)
  }
}

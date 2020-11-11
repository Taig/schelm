package io.taig.schelm.data

import io.taig.schelm.util.NodeFunctor
import io.taig.schelm.util.NodeFunctor.ops._

final case class Html[F[_]](value: Node[F, Html[F]]) extends AnyVal

object Html {
  implicit def functor[F[_]]: NodeFunctor[Html[F]] = new NodeFunctor[Html[F]] {
    override def mapAttributes(html: Html[F])(f: Attributes => Attributes): Html[F] =
      Html(html.value.mapAttributes(f))
  }
}

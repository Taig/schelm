package io.taig.schelm

import cats.effect.Concurrent
import cats.implicits._
import org.jsoup.nodes.{Node => JNode}

object HtmlServerSchelm {
  def apply[F[_]: Concurrent, Event, Node]: F[Schelm[F, Event, JNode]] =
    ServerDom[F, Event].map(new HtmlSchelm(_, EventManager.noop[F, Event]))
}

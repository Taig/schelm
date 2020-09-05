package io.taig.schelm.css.interpreter

import cats.Applicative
import io.taig.schelm.css.data.{CssHtmlDiff, Selector, Style}
import io.taig.schelm.data.Patcher

final class CssHtmlPatcher[F[_]: Applicative, Event, Node]
    extends Patcher[F, (List[Node], Map[Selector, Style]), CssHtmlDiff[Event]] {
  override def patch(structure: (List[Node], Map[Selector, Style]), diff: CssHtmlDiff[Event]): F[Unit] =
    Applicative[F].unit
}

object CssHtmlPatcher {
  def apply[F[_]: Applicative, Event, Node]: Patcher[F, (List[Node], Map[Selector, Style]), CssHtmlDiff[Event]] =
    new CssHtmlPatcher[F, Event, Node]
}

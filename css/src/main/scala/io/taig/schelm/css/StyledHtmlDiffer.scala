package io.taig.schelm.css

import cats.data.Ior
import io.taig.schelm._

final class StyledHtmlDiffer[A](differ: Differ[Html[A], HtmlDiff[A]])
    extends Differ[StyledHtml[A], StyledHtmlDiff[A]] {
  override def diff(
      previous: StyledHtml[A],
      next: StyledHtml[A]
  ): Option[StyledHtmlDiff[A]] = {
    // TODO Stylesheet diff
    differ.diff(toHtml(previous), toHtml(next)).map(Ior.left)
  }
}

object StyledHtmlDiffer {
  def apply[A]: Differ[StyledHtml[A], StyledHtmlDiff[A]] =
    new StyledHtmlDiffer[A](HtmlDiffer[A])
}

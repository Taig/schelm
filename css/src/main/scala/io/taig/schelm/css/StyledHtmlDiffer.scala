package io.taig.schelm.css

import io.taig.schelm.{Diff, Differ, Html, HtmlDiffer}

final class StyledHtmlDiffer[A](differ: Differ[Html[A], Diff[A]])
    extends Differ[StyledHtml[A], StyledDiff[A]] {
  override def diff(
      previous: StyledHtml[A],
      next: StyledHtml[A]
  ): Option[StyledDiff[A]] =
    differ.diff(previous.html, next.html).map { diff =>
      StyledDiff(diff, next.stylesheet)
    }
}

object StyledHtmlDiffer {
  def apply[A]: Differ[StyledHtml[A], StyledDiff[A]] =
    new StyledHtmlDiffer[A](HtmlDiffer[A])
}

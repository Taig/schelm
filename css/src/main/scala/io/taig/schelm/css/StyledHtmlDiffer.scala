package io.taig.schelm.css

import cats.data.Ior
import cats.implicits._
import io.taig.schelm._

final class StyledHtmlDiffer[A](
    html: Differ[Html[A], HtmlDiff[A]],
    stylesheet: Differ[StyledHtml[A], StylesheetDiff]
) extends Differ[StyledHtml[A], StyledHtmlDiff[A]] {
  override def diff(
      previous: StyledHtml[A],
      next: StyledHtml[A]
  ): Option[StyledHtmlDiff[A]] =
    (html.diff(toHtml(previous), toHtml(next)), stylesheet.diff(previous, next)) match {
      case (Some(html), Some(styles)) => Ior.both(html, styles).some
      case (Some(html), None)         => Ior.left(html).some
      case (None, Some(html))         => Ior.right(html).some
      case (None, None)               => None
    }
}

object StyledHtmlDiffer {
  def apply[A]: Differ[StyledHtml[A], StyledHtmlDiff[A]] =
    new StyledHtmlDiffer[A](HtmlDiffer[A], StylesheetDiffer[A])
}

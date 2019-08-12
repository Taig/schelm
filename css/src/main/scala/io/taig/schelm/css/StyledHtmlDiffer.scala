package io.taig.schelm.css

import cats.data.Ior
import cats.implicits._
import io.taig.schelm._

final class StyledHtmlDiffer[A](
    html: Differ[Html[A], HtmlDiff[A]],
    stylesheet: Differ[StylesheetWidget[A], StylesheetDiff]
) extends Differ[StylesheetWidget[A], StyledHtmlDiff[A]] {
  override def diff(
      previous: StylesheetWidget[A],
      next: StylesheetWidget[A]
  ): Option[StyledHtmlDiff[A]] =
    (html.diff(toHtml(previous), toHtml(next)), stylesheet.diff(previous, next)) match {
      case (Some(html), Some(stylesheet)) => Ior.both(html, stylesheet).some
      case (Some(html), None)             => Ior.left(html).some
      case (None, Some(stylesheet))       => Ior.right(stylesheet).some
      case (None, None)                   => None
    }
}

object StyledHtmlDiffer {
  def apply[A]: Differ[StylesheetWidget[A], StyledHtmlDiff[A]] =
    new StyledHtmlDiffer[A](HtmlDiffer[A], StylesheetDiffer[A])
}

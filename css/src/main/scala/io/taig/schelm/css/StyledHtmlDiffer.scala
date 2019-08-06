package io.taig.schelm.css

import cats.data.Ior
import cats.implicits._
import io.taig.schelm._

final class StyledHtmlDiffer[Event](
    html: Differ[Html[Event], HtmlDiff[Event]],
    stylesheet: Differ[StyledHtml[_], StylesheetDiff]
) extends Differ[StyledHtml[Event], StyledHtmlDiff[Event]] {
  override def diff(
      previous: StyledHtml[Event],
      next: StyledHtml[Event]
  ): Option[StyledHtmlDiff[Event]] =
    (html.diff(toHtml(previous), toHtml(next)), stylesheet.diff(previous, next)) match {
      case (Some(html), Some(stylesheet)) => Ior.both(html, stylesheet).some
      case (Some(html), None)             => Ior.left(html).some
      case (None, Some(stylesheet))       => Ior.right(stylesheet).some
      case (None, None)                   => None
    }
}

object StyledHtmlDiffer {
  def apply[Event]: Differ[StyledHtml[Event], StyledHtmlDiff[Event]] =
    new StyledHtmlDiffer[Event](HtmlDiffer[Event], StylesheetDiffer)
}

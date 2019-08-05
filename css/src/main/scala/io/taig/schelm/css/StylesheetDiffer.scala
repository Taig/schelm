package io.taig.schelm.css

import io.taig.schelm.Differ

final class StylesheetDiffer[A] extends Differ[StyledHtml[A], StylesheetDiff] {
  override def diff(
      previous: StyledHtml[A],
      next: StyledHtml[A]
  ): Option[StylesheetDiff] = None
}

object StylesheetDiffer {
  def apply[A]: Differ[StyledHtml[A], StylesheetDiff] = new StylesheetDiffer[A]
}

package io.taig.schelm.css

import io.taig.schelm.Differ

object StylesheetDiffer extends Differ[StyledHtml[_], StylesheetDiff] {
  override def diff(
      previous: StyledHtml[_],
      next: StyledHtml[_]
  ): Option[StylesheetDiff] = None
}

package io.taig.schelm.css

import io.taig.schelm.Differ

final class StylesheetDiffer[A] extends Differ[StyledHtml[A], StylesheetDiff] {
  override def diff(
      previous: StyledHtml[A],
      next: StyledHtml[A]
  ): Option[StylesheetDiff] = {
    // TODO recurse on children!
    val left = previous.head.toSet
    val right = next.head.toSet
    val removed = left diff right
    val added = right diff left
    val diffs = removed.map(StylesheetDiff.RemoveRule) ++
      added.map(StylesheetDiff.AddRule)
    StylesheetDiff.from(diffs)
  }
}

object StylesheetDiffer {
  def apply[A]: Differ[StyledHtml[A], StylesheetDiff] = new StylesheetDiffer[A]
}

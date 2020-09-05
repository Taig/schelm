package io.taig.schelm.css.data

sealed abstract class CssDiff extends Product with Serializable

object CssDiff {
  final case class Add(selector: Selector, style: Style) extends CssDiff
  final case class Remove(selector: Selector) extends CssDiff
}

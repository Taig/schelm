package io.taig.schelm.dsl

final class CssTimeUnitOps(val value: String) extends AnyVal {
  def s: String = value + "s"
  @inline
  def second: String = s
  @inline
  def seconds: String = s
  def ms: String = value + "ms"
  @inline
  def millisecond: String = ms
  @inline
  def milliseconds: String = ms
}

package io.taig.schelm.algebra

abstract class Differ[Structure, Diff] {
  def diff(current: Structure, next: Structure): Option[Diff]
}

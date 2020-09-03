package io.taig.schelm.algebra

abstract class Differ[Structure, Diff] {
  def diff(previous: Structure, next: Structure): Option[Diff]
}

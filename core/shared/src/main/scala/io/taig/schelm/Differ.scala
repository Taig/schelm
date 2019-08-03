package io.taig.schelm

abstract class Differ[A, B] {
  def diff(previous: A, next: A): Option[B]
}

package io.taig.schelm.algebra

abstract class Printer[A] {
  def print(value: A, pretty: Boolean): String
}

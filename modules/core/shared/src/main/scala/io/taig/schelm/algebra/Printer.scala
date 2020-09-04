package io.taig.schelm.algebra

abstract class Printer[Structure] {
  def print(structure: Structure): String
}

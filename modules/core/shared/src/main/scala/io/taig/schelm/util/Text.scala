package io.taig.schelm.util

object Text {
  val Linebreak = '\n'

  def indent(n: Int)(value: String): String =
    if (n == 0) value
    else {
      val result = new StringBuilder()
      val indent = " " * n

      value.split(Linebreak).foreach(line => result.append(indent).append(line).append(Linebreak))
      if (result.nonEmpty) result.deleteCharAt(result.length() - 1)
      result.result()
    }

  def align(n: Int)(value: String): String = {
    if (n == 0) value
    else if (!value.contains(Linebreak)) value
    else {
      val lines = value.split(Linebreak)

      if (lines.isEmpty) value
      else {
        val result = new StringBuilder(lines.head).append(Linebreak)
        val indent = " " * n

        lines.tail.foreach { line => result.append(indent).append(line).append(Linebreak) }
        result.deleteCharAt(result.length() - 1)

        result.result()
      }
    }
  }
}

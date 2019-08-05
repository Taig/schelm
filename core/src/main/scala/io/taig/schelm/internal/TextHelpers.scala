package io.taig.schelm.internal

object TextHelpers {
  def ident(columns: Int, value: String): String = {
    val whitespace = " " * columns
    val builder = new StringBuilder
    val lines = value.split("\n")

    lines.zipWithIndex.foreach {
      case (line, index) =>
        builder.append(whitespace).append(line)
        if (index < lines.length - 1) builder.append('\n')
    }
    builder.toString()
  }
}

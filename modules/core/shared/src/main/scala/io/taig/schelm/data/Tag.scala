package io.taig.schelm.data

import io.taig.schelm.util.Text

final case class Tag[F[_]](name: Tag.Name, attributes: Attributes, listeners: Listeners[F]) {
  private[schelm] def toString(f: String => String): String = {
    if (attributes.isEmpty && listeners.isEmpty) s"""${f(name.value)}"""
    else
      s"""{
         |  name = ${f(name.value)},
         |  attributes = ${Text.align(2)(attributes.toString)},
         |  listeners = []
         |}""".stripMargin
  }

  override def toString: String = toString(tag => s"<$tag>")
}

object Tag {
  final case class Name(value: String) extends AnyVal
}

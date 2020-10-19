package io.taig.schelm.css.util

import io.taig.schelm.css.data.{Css, Style}
import simulacrum.typeclass

@typeclass
trait ModifyCss[A] {
  def modifyCss(fa: A)(f: Style => Style): A
}

object ModifyCss {
  implicit def css[A]: ModifyCss[Css[A]] = new ModifyCss[Css[A]] {
    override def modifyCss(fa: Css[A])(f: Style => Style): Css[A] = fa.copy(style = f(fa.style))
  }
}

package io.taig.schelm.css.data

import io.taig.schelm.css.instance.StyledInstances

final case class Css[+A](style: Style, value: A)

object Css extends StyledInstances {
  def unstyled[A](value: A): Css[A] = Css(Style.Empty, value)
}

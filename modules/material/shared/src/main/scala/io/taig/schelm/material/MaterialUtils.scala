package io.taig.schelm.material

import io.taig.schelm.css.data.Declaration

object MaterialUtils {
  def transition(name: Declaration.Name): String = s"${name.value} 250ms cubic-bezier(0.4, 0, 0.2, 1) 0ms"
}

package io.taig.schelm.mdc

import cats.effect.IO
import io.taig.schelm.data.{Callback, Platform}

object Mdc {
  val chip: Callback.Element = Callback.Element.noop
}

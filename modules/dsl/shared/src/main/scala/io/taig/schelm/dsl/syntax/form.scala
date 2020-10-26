package io.taig.schelm.dsl.syntax

import io.taig.schelm.dsl.Widget
import io.taig.schelm.dsl.syntax.html.input

trait form {
  object ManagedInput {
    def apply[F[_]](value: String): Widget[F, Nothing, Any] = ???
  }
}

object form extends form

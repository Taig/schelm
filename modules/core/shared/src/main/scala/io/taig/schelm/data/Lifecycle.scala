package io.taig.schelm.data

import cats.effect.{IO, Resource}

object Lifecycle {
  abstract class Element[+Event] {
    def apply[A](platform: Platform)(element: platform.Element): Resource[IO, A]
  }

  abstract class Fragment[+Event] {
    def apply[A](platform: Platform)(nodes: List[platform.Node]): Resource[IO, A]
  }

  abstract class Text[+Event] {
    def apply[A](platform: Platform)(text: platform.Text): Resource[IO, A]
  }
}

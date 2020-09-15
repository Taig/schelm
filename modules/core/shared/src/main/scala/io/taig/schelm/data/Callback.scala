package io.taig.schelm.data

import cats.effect.IO

object Callback {
  abstract class Element {
    def apply(platform: Platform)(element: platform.Element): IO[Unit]
  }

  object Element {
    val noop: Callback.Element = new Callback.Element {
      override def apply(platform: Platform)(element: platform.Element): IO[Unit] = IO.unit
    }
  }

  abstract class Fragment {
    def apply(platform: Platform)(nodes: List[platform.Node]): IO[Unit]
  }

  object Fragment {
    val noop: Callback.Fragment = new Callback.Fragment {
      override def apply(platform: Platform)(nodes: List[platform.Node]): IO[Unit] = IO.unit
    }
  }

  abstract class Text {
    def apply(platform: Platform)(text: platform.Text): IO[Unit]
  }
}

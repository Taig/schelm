package io.taig.schelm.data

import cats.effect.{IO, Resource}

object Lifecycle {
  private val unit = Resource.pure[IO, Unit](())

  abstract class Element {
    def apply(platform: Platform)(element: platform.Element): Resource[IO, Unit]
  }

  object Element {
    val noop: Lifecycle.Element = new Lifecycle.Element {
      override def apply(platform: Platform)(element: platform.Element): Resource[IO, Unit] = unit
    }
  }

  abstract class Fragment {
    def apply(platform: Platform)(nodes: List[platform.Node]): Resource[IO, Unit]
  }

  object Fragment {
    val noop: Lifecycle.Fragment = new Lifecycle.Fragment {
      override def apply(platform: Platform)(nodes: List[platform.Node]): Resource[IO, Unit] = unit
    }
  }

  abstract class Text {
    def apply(platform: Platform)(text: platform.Text): Resource[IO, Unit]
  }

  object Text {
    val noop: Lifecycle.Text = new Lifecycle.Text {
      override def apply(platform: Platform)(text: platform.Text): Resource[IO, Unit] = unit
    }
  }
}

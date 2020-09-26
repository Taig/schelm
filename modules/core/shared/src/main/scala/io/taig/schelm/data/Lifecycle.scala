package io.taig.schelm.data

import cats.effect.{IO, Resource}
import io.taig.schelm.algebra.Dom

object Lifecycle {
  private val unit = Resource.pure[IO, Unit](())

  abstract class Element {
    def apply(element: Dom.Element): Resource[IO, Unit]
  }

  object Element {
    val noop: Lifecycle.Element = new Lifecycle.Element {
      override def apply(element: Dom.Element): Resource[IO, Unit] = unit
    }
  }

  abstract class Fragment {
    def apply(nodes: List[Dom.Node]): Resource[IO, Unit]
  }

  object Fragment {
    val noop: Lifecycle.Fragment = new Lifecycle.Fragment {
      override def apply(nodes: List[Dom.Node]): Resource[IO, Unit] = unit
    }
  }

  abstract class Text {
    def apply(text: Dom.Text): Resource[IO, Unit]
  }

  object Text {
    val noop: Lifecycle.Text = new Lifecycle.Text {
      override def apply(text: Dom.Text): Resource[IO, Unit] = unit
    }
  }
}

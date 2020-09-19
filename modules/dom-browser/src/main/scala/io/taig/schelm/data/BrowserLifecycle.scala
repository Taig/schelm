package io.taig.schelm.data

import cats.effect.{IO, Resource}
import org.scalajs.dom

object BrowserLifecycle {
  private val unit = Resource.pure[IO, Unit](())

  def element(f: dom.Element => Resource[IO, Unit]): Lifecycle.Element =
    new Lifecycle.Element {
      override def apply(platform: Platform)(element: platform.Element): Resource[IO, Unit] =
        if (platform.isJs) f(element.asInstanceOf[dom.Element]) else unit
    }
}

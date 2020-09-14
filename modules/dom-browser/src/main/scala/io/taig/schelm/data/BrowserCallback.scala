package io.taig.schelm.data
import cats.effect.IO
import org.scalajs.dom

object BrowserCallback {
  def element(f: dom.Element => IO[Unit]): Callback.Element =
    new Callback.Element {
      override def apply(platform: Platform)(element: platform.Element): IO[Unit] =
        if(platform.isJs) f(element.asInstanceOf[dom.Element]) else IO.unit
    }
}

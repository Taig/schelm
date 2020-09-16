package io.taig.schelm.data

import org.scalajs.dom

object BrowserCallback {
  def element[Event](f: dom.Element => Event): Callback.Element[Event] =
    new Callback.Element[Event] {
      override def apply(platform: Platform)(element: platform.Element): Option[Event] =
        if (platform.isJs) Some(f(element.asInstanceOf[dom.Element])) else None
    }
}

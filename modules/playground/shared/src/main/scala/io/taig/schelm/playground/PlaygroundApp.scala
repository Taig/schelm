package io.taig.schelm.playground

import cats.effect.Sync
import cats.implicits._
import io.taig.schelm.data._
import io.taig.schelm.dsl._
import io.taig.schelm.dsl.data.DslWidget
import io.taig.schelm.mdc.{MdcChip, MdcChipSet, MdcTheme, MdcTopAppBar}

// TODO: think about hydration: render the html on the server, and send to client. Then render again on the client with
// same state, but afterwards remove all listeners. Then patch this version to only add the listeners, everything else
// should be in place already
object PlaygroundApp {
  def render[F[_]: Sync](label: String): DslWidget[F, MdcTheme] = {
    fragment(
      children = Children.of(
        MdcTopAppBar.regular(title = "Yolo"),
        div(
          attributes = Attributes.of(a.cls := "mdc-top-app-bar--fixed-adjust"),
          children = Children.of(
            MdcChipSet(chips =
              Children.of(
                MdcChip(label, tabindex = 1, icon = ("event", MdcChip.Icon.Position.Leading).some),
                MdcChip("hello google", tabindex = 2, icon = ("event", MdcChip.Icon.Position.Trailing).some)
              )
            )
          )
        )
      )
    )
  }
}

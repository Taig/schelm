package io.taig.schelm.documentation

import io.taig.schelm.css.data.Style
import io.taig.schelm.data.{Attributes, Children}
import io.taig.schelm.dsl._
import io.taig.schelm.dsl.data.DslNode
import io.taig.schelm.flexboxgrid.{GridCol, GridRow}
import io.taig.schelm.mdc.{MdcTheme, MdcTopAppBar}

object App {
  def apply[F[_]](state: State): DslNode[F, Event, MdcTheme] =
    fragment(
      children = Children.of(
        MdcTopAppBar.regular(
          "Schelm",
          style = Style.of(
            boxShadow := "0px 2px 4px -1px rgba(0,0,0,0.2), 0px 4px 5px 0px rgba(0,0,0,0.14), 0px 1px 10px 0px rgba(0,0,0,0.12)"
          )
        ),
        main(
          attributes = Attributes.of(a.cls := "mdc-top-app-bar--fixed-adjust"),
          style = Style.of(
            maxWidth := "960px",
            margin := "0 auto"
          ),
          children = Children.of(
            GridRow.default(
              children = Children.of(
                GridCol(
                  div(
                    children = Children.of(
                      text("Hallo")
                    )
                  )
                )
              )
            )
          )
        )
      )
    )
}

package io.taig.schelm.material

import io.taig.schelm.dsl._

object MaterialElevation {
  private val Color1 = "rgba(0,0,0,0.2)"
  private val Color2 = "rgba(0,0,0,0.14)"
  private val Color3 = "rgba(0,0,0,0.12)"

  val ShadowNormal = s"0px 3px 1px -2px $Color1,0px 2px 2px 0px $Color2,0px 1px 5px 0px $Color3"
  val ShadowHover = s"0px 2px 4px -1px $Color1,0px 4px 5px 0px $Color2,0px 1px 10px 0px $Color3"
  val ShadowActive = s"0px 5px 5px -3px $Color1,0px 8px 10px 1px $Color2,0px 3px 14px 2px $Color3"

  val Transition: String = MaterialUtils.transition(boxShadow)

  def apply[F[_], Event, Context](
      widget: Widget[F, Event, Context],
      clickable: Boolean = false
  ): Widget[F, Event, Context] = {
    val styles =
      if (clickable)
        css(boxShadow := ShadowNormal)
          .&(hover)(boxShadow := ShadowHover)
          .&(active)(boxShadow := ShadowActive)
      else css(boxShadow := ShadowNormal)

    widget.modifyStyle(previous => (previous ++ styles).concat(css(transition := Transition), divider = ","))
  }
}

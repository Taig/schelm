package io.taig.schelm.material

import io.taig.color.Color
import io.taig.color.implicits._
import io.taig.schelm.material.MaterialTheme.Mode

final case class MaterialTheme(
    mode: MaterialTheme.Mode,
    context: MaterialTheme.Context,
    light: MaterialTheme.Variant,
    dark: MaterialTheme.Variant
) {
  def variant: MaterialTheme.Variant = mode match {
    case Mode.Light => light
    case Mode.Dark  => dark
  }
}

object MaterialTheme {
  sealed abstract class Mode extends Product with Serializable

  object Mode {
    final case object Light extends Mode
    final case object Dark extends Mode
  }

  sealed abstract class Context extends Product with Serializable

  object Context {
    final case object Surface extends Context
    final case object Paper extends Context
    final case object Primary extends Context
    final case object Secondary extends Context
  }

  final case class Variant(spacing: Int, buttons: Buttons)

  final case class Buttons(normal: Button, primary: Button, secondary: Button, danger: Button)

  final case class Button(
      background: Color,
      radius: String,
      shadow: String,
      font: Font,
      hover: Button.Effect,
      active: Button.Effect
  )

  object Button {
    final case class Effect(background: Color, shadow: String)

    def default(text: Color, background: Color): Button = Button(
      background = background,
      radius = "4px",
      shadow = "0px 3px 1px -2px rgba(0,0,0,0.2), 0px 2px 2px 0px rgba(0,0,0,0.14), 0px 1px 5px 0px rgba(0,0,0,0.12)",
      font = Font(
        family = Font.DefaultFont,
        color = text,
        size = "0.875rem",
        weight = "500",
        lineHeight = "1.75",
        letterSpacing = "0.02857em",
        transform = Some("uppercase")
      ),
      hover = Effect(
        background = background.darken(0.75f),
        shadow = "0px 2px 4px -1px rgba(0,0,0,0.2), 0px 4px 5px 0px rgba(0,0,0,0.14), 0px 1px 10px 0px rgba(0,0,0,0.12)"
      ),
      active = Effect(
        background = background.darken(0.5f),
        shadow =
          "0px 5px 5px -3px rgba(0,0,0,0.2), 0px 8px 10px 1px rgba(0,0,0,0.14), 0px 3px 14px 2px rgba(0,0,0,0.12)"
      )
    )
  }

  final case class Font(
      family: String,
      color: Color,
      size: String,
      weight: String,
      lineHeight: String,
      letterSpacing: String,
      transform: Option[String]
  )

  object Font {
    val DefaultFont: String = "'Roboto', sans-serif"
  }

  val Default: MaterialTheme = MaterialTheme(
    mode = Mode.Light,
    context = Context.Surface,
    light = Variant(
      spacing = 8,
      buttons = Buttons(
        normal = Button.default(rgb"#000000de", rgb"#e0e0e0"),
        primary = Button.default(rgb"#ffffff", rgb"#1976d2"),
        secondary = Button.default(rgb"#ffffff", rgb"#1976d2"),
        danger = Button.default(rgb"#ffffff", rgb"#1976d2")
      )
    ),
    dark = Variant(
      spacing = 8,
      buttons = Buttons(
        normal = Button.default(rgb"#000000de", rgb"#e0e0e0"),
        primary = Button.default(rgb"#ffffff", rgb"#1976d2"),
        secondary = Button.default(rgb"#ffffff", rgb"#1976d2"),
        danger = Button.default(rgb"#ffffff", rgb"#1976d2")
      )
    )
  )
}

package io.taig.schelm.material

import io.taig.color.Color
import io.taig.color.implicits._
import io.taig.schelm.material.MaterialTheme.{Context, Mode}

final case class MaterialTheme(
    mode: MaterialTheme.Mode,
    context: MaterialTheme.Context,
    spacing: Int,
    light: MaterialTheme.Flavor,
    dark: MaterialTheme.Flavor
) {
  def flavor: MaterialTheme.Flavor = mode match {
    case Mode.Light => light
    case Mode.Dark  => dark
  }

  def variant: MaterialTheme.Variant = context match {
    case Context.Surface   => flavor.surface
    case Context.Paper     => flavor.paper
    case Context.Primary   => flavor.primary
    case Context.Secondary => flavor.secondary
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

  final case class Flavor(palette: Palette, surface: Variant, paper: Variant, primary: Variant, secondary: Variant)

  final case class Palette(primary: Shade, secondary: Shade)

  final case class Shade(light: Color, normal: Color, dark: Color)

  final case class Variant(buttons: Buttons)

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
        background = background.darker(0.75f),
        shadow = "0px 2px 4px -1px rgba(0,0,0,0.2),0px 4px 5px 0px rgba(0,0,0,0.14),0px 1px 10px 0px rgba(0,0,0,0.12)"
      ),
      active = Effect(
        background = background.darker(0.5f),
        shadow = "0px 5px 5px -3px rgba(0,0,0,0.2),0px 8px 10px 1px rgba(0,0,0,0.14),0px 3px 14px 2px rgba(0,0,0,0.12)"
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

  def derive(primary: Color, secondary: Color): MaterialTheme = {
    MaterialTheme(
      mode = Mode.Light,
      context = Context.Surface,
      spacing = 8,
      light = Flavor(
        palette = Palette(
          primary = Shade(
            light = primary.brighter(),
            normal = primary,
            dark = primary.darker()
          ),
          secondary = ???
        ),
        surface = Variant(
          buttons = Buttons(
            normal = Button.default(rgb"#000000de", rgb"#e0e0e0"),
            primary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            secondary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            danger = Button.default(rgb"#ffffff", rgb"#1976d2")
          )
        ),
        paper = Variant(
          buttons = Buttons(
            normal = Button.default(rgb"#000000de", rgb"#e0e0e0"),
            primary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            secondary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            danger = Button.default(rgb"#ffffff", rgb"#1976d2")
          )
        ),
        primary = Variant(
          buttons = Buttons(
            normal = Button.default(rgb"#000000de", rgb"#e0e0e0"),
            primary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            secondary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            danger = Button.default(rgb"#ffffff", rgb"#1976d2")
          )
        ),
        secondary = Variant(
          buttons = Buttons(
            normal = Button.default(rgb"#000000de", rgb"#e0e0e0"),
            primary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            secondary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            danger = Button.default(rgb"#ffffff", rgb"#1976d2")
          )
        )
      ),
      dark = Flavor(
        palette = ???,
        surface = Variant(
          buttons = Buttons(
            normal = Button.default(rgb"#000000de", rgb"#e0e0e0"),
            primary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            secondary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            danger = Button.default(rgb"#ffffff", rgb"#1976d2")
          )
        ),
        paper = Variant(
          buttons = Buttons(
            normal = Button.default(rgb"#000000de", rgb"#e0e0e0"),
            primary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            secondary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            danger = Button.default(rgb"#ffffff", rgb"#1976d2")
          )
        ),
        primary = Variant(
          buttons = Buttons(
            normal = Button.default(rgb"#000000de", rgb"#e0e0e0"),
            primary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            secondary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            danger = Button.default(rgb"#ffffff", rgb"#1976d2")
          )
        ),
        secondary = Variant(
          buttons = Buttons(
            normal = Button.default(rgb"#000000de", rgb"#e0e0e0"),
            primary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            secondary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            danger = Button.default(rgb"#ffffff", rgb"#1976d2")
          )
        )
      )
    )
  }

  val Default: MaterialTheme = MaterialTheme(
    mode = Mode.Light,
    context = Context.Surface,
    spacing = 8,
    light = Flavor(
      palette = ???,
      surface = Variant(
        buttons = Buttons(
          normal = Button.default(rgb"#000000de", rgb"#e0e0e0"),
          primary = Button.default(rgb"#ffffff", rgb"#1976d2"),
          secondary = Button.default(rgb"#ffffff", rgb"#1976d2"),
          danger = Button.default(rgb"#ffffff", rgb"#1976d2")
        )
      ),
      paper = Variant(
        buttons = Buttons(
          normal = Button.default(rgb"#000000de", rgb"#e0e0e0"),
          primary = Button.default(rgb"#ffffff", rgb"#1976d2"),
          secondary = Button.default(rgb"#ffffff", rgb"#1976d2"),
          danger = Button.default(rgb"#ffffff", rgb"#1976d2")
        )
      ),
      primary = Variant(
        buttons = Buttons(
          normal = Button.default(rgb"#000000de", rgb"#e0e0e0"),
          primary = Button.default(rgb"#ffffff", rgb"#1976d2"),
          secondary = Button.default(rgb"#ffffff", rgb"#1976d2"),
          danger = Button.default(rgb"#ffffff", rgb"#1976d2")
        )
      ),
      secondary = Variant(
        buttons = Buttons(
          normal = Button.default(rgb"#000000de", rgb"#e0e0e0"),
          primary = Button.default(rgb"#ffffff", rgb"#1976d2"),
          secondary = Button.default(rgb"#ffffff", rgb"#1976d2"),
          danger = Button.default(rgb"#ffffff", rgb"#1976d2")
        )
      )
    ),
    dark = Flavor(
      palette = ???,
      surface = Variant(
        buttons = Buttons(
          normal = Button.default(rgb"#000000de", rgb"#e0e0e0"),
          primary = Button.default(rgb"#ffffff", rgb"#1976d2"),
          secondary = Button.default(rgb"#ffffff", rgb"#1976d2"),
          danger = Button.default(rgb"#ffffff", rgb"#1976d2")
        )
      ),
      paper = Variant(
        buttons = Buttons(
          normal = Button.default(rgb"#000000de", rgb"#e0e0e0"),
          primary = Button.default(rgb"#ffffff", rgb"#1976d2"),
          secondary = Button.default(rgb"#ffffff", rgb"#1976d2"),
          danger = Button.default(rgb"#ffffff", rgb"#1976d2")
        )
      ),
      primary = Variant(
        buttons = Buttons(
          normal = Button.default(rgb"#000000de", rgb"#e0e0e0"),
          primary = Button.default(rgb"#ffffff", rgb"#1976d2"),
          secondary = Button.default(rgb"#ffffff", rgb"#1976d2"),
          danger = Button.default(rgb"#ffffff", rgb"#1976d2")
        )
      ),
      secondary = Variant(
        buttons = Buttons(
          normal = Button.default(rgb"#000000de", rgb"#e0e0e0"),
          primary = Button.default(rgb"#ffffff", rgb"#1976d2"),
          secondary = Button.default(rgb"#ffffff", rgb"#1976d2"),
          danger = Button.default(rgb"#ffffff", rgb"#1976d2")
        )
      )
    )
  )
}

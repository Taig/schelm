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
  val DefaultFontFamily: String = "'Roboto', sans-serif"

  val DefaultRadius = "4px"

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

  final case class Palette(primary: Shade, secondary: Shade, surface: Color, paper: Color, neutral: Color)

  final case class Shade(light: Color, normal: Color, dark: Color)

  final case class Variant(typography: Typography, buttons: Buttons, input: Input)

  object Variant {
    def default(primary: Color, text: Color): Variant = {
      val typography = Typography.default(text) // rgb"#000000de"

      Variant(
        typography = typography,
        buttons = Buttons(
          normal = Button.default(rgb"#000000de", rgb"#e0e0e0"),
          primary = Button.default(rgb"#ffffff", rgb"#1976d2"),
          secondary = Button.default(rgb"#ffffff", rgb"#1976d2"),
          danger = Button.default(rgb"#ffffff", rgb"#1976d2")
        ),
        input = Input(
          background = None,
          border = rgb"#0000003b",
          focus = palette.primary.normal,
          radius = DefaultRadius,
          font = ???
        )
      )
    }
  }

  final case class Typography(
      h1: Font,
      h2: Font,
      h3: Font,
      h4: Font,
      h5: Font,
      h6: Font,
      subtitle1: Font,
      subtitle2: Font,
      body1: Font,
      body2: Font,
      button: Font,
      caption: Font,
      overline: Font
  )

  object Typography {
    def default(color: Color): Typography = Typography(
      h1 = Font(
        family = Font.DefaultFontFamily,
        color,
        size = "6rem",
        weight = "300",
        lineHeight = "6rem",
        letterSpacing = "-0.015625em",
        transform = None
      ),
      h2 = Font(
        family = Font.DefaultFontFamily,
        color,
        size = "3.75rem",
        weight = "300",
        lineHeight = "3.75rem",
        letterSpacing = "-0.0083333333em",
        transform = None
      ),
      h3 = Font(
        family = Font.DefaultFontFamily,
        color,
        size = "3rem",
        weight = "400",
        lineHeight = "3.125rem",
        letterSpacing = "normal",
        transform = None
      ),
      h4 = Font(
        family = Font.DefaultFontFamily,
        color,
        size = "2.125rem",
        weight = "400",
        lineHeight = "2.5rem",
        letterSpacing = "0.0073529412em",
        transform = None
      ),
      h5 = Font(
        family = Font.DefaultFontFamily,
        color,
        size = "2rem",
        weight = "400",
        lineHeight = "2rem",
        letterSpacing = "normal",
        transform = None
      ),
      h6 = Font(
        family = Font.DefaultFontFamily,
        color,
        size = "1.25rem",
        weight = "500",
        lineHeight = "2rem",
        letterSpacing = "0.0125rem",
        transform = None
      ),
      subtitle1 = Font(
        family = Font.DefaultFontFamily,
        color,
        size = "1rem",
        weight = "400",
        lineHeight = "1.75rem",
        letterSpacing = "0.009375em",
        transform = None
      ),
      subtitle2 = Font(
        family = Font.DefaultFontFamily,
        color,
        size = "0.875rem",
        weight = "500",
        lineHeight = "1.375rem",
        letterSpacing = "0.0071428571em",
        transform = None
      ),
      body1 = Font(
        family = Font.DefaultFontFamily,
        color,
        size = "1rem",
        weight = "400",
        lineHeight = "1.5rem",
        letterSpacing = "0.03125rem",
        transform = None
      ),
      body2 = Font(
        family = Font.DefaultFontFamily,
        color,
        size = "0.75rem",
        weight = "400",
        lineHeight = "1.25rem",
        letterSpacing = "0.0178571429em",
        transform = None
      ),
      button = Font(
        family = Font.DefaultFontFamily,
        color,
        size = "0.875rem",
        weight = "500",
        lineHeight = "2.25rem",
        letterSpacing = "0.0892857143em",
        transform = Some("uppercase")
      ),
      caption = Font(
        family = Font.DefaultFontFamily,
        color,
        size = "0.75rem",
        weight = "400",
        lineHeight = "1.25rem",
        letterSpacing = "0.0333333333em",
        transform = None
      ),
      overline = Font(
        family = Font.DefaultFontFamily,
        color,
        size = "0.75rem",
        weight = "500",
        lineHeight = "2em",
        letterSpacing = "0.1666666667em",
        transform = Some("uppercase")
      )
    )
  }

  final case class Buttons(normal: Button, primary: Button, secondary: Button, danger: Button)

  final case class Button(
      background: Color,
      radius: String,
      font: Font,
      hover: Button.Effect,
      active: Button.Effect
  )

  object Button {
    final case class Effect(background: Color)

    def default(text: Color, background: Color): Button = Button(
      background = background,
      radius = DefaultRadius,
      font = Font(
        family = Font.DefaultFontFamily,
        color = text,
        size = "0.875rem",
        weight = "500",
        lineHeight = "1.75",
        letterSpacing = "0.02857em",
        transform = Some("uppercase")
      ),
      hover = Effect(background = background.darker(0.75f)),
      active = Effect(background = background.darker(0.5f))
    )
  }

  final case class Input(background: Option[Color], border: Color, focus: Color, radius: String, font: Font)

  final case class Font(
      family: String,
      color: Color,
      size: String,
      weight: String,
      lineHeight: String,
      letterSpacing: String,
      transform: Option[String]
  )

  def derive(primary: Color, secondary: Color): MaterialTheme = {
    val light = Palette(
      primary = Shade(
        light = primary.brighter(),
        normal = primary,
        dark = primary.darker()
      ),
      secondary = Shade(
        light = primary.brighter(),
        normal = primary,
        dark = primary.darker()
      ),
      surface = rgb"#f7f7f7",
      paper = rgb"#ffffff",
      neutral = rgb"#e0e0e0"
    )

    val dark = Palette(
      primary = Shade(
        light = primary.brighter(),
        normal = primary,
        dark = primary.darker()
      ),
      secondary = Shade(
        light = primary.brighter(),
        normal = primary,
        dark = primary.darker()
      ),
      surface = rgb"#121212",
      paper = rgb"#333333",
      neutral = rgb"#e0e0e0"
    )

    MaterialTheme(
      mode = Mode.Light,
      context = Context.Surface,
      spacing = 8,
      light = Flavor(
        palette = light,
        surface = Variant(
          typography = Typography.default(rgb"#000000de"),
          buttons = Buttons(
            normal = Button.default(rgb"#000000de", rgb"#e0e0e0"),
            primary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            secondary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            danger = Button.default(rgb"#ffffff", rgb"#1976d2")
          ),
          input = Input(
            background = None,
            border = rgb"#0000003b",
            focus = light.primary.normal,
            radius = DefaultRadius,
            font = ???
          )
        ),
        paper = Variant(
          typography = Typography.default(rgb"#000000de"),
          buttons = Buttons(
            normal = Button.default(rgb"#000000de", rgb"#e0e0e0"),
            primary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            secondary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            danger = Button.default(rgb"#ffffff", rgb"#1976d2")
          ),
          input = ???
        ),
        primary = Variant(
          typography = Typography.default(rgb"#ffffff"),
          buttons = Buttons(
            normal = Button.default(rgb"#000000de", rgb"#e0e0e0"),
            primary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            secondary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            danger = Button.default(rgb"#ffffff", rgb"#1976d2")
          ),
          input = ???
        ),
        secondary = Variant(
          typography = Typography.default(rgb"#ffffff"),
          buttons = Buttons(
            normal = Button.default(rgb"#000000de", rgb"#e0e0e0"),
            primary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            secondary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            danger = Button.default(rgb"#ffffff", rgb"#1976d2")
          ),
          input = ???
        )
      ),
      dark = Flavor(
        palette = dark,
        surface = Variant(
          typography = Typography.default(rgb"#ffffff"),
          buttons = Buttons(
            normal = Button.default(rgb"#000000de", rgb"#e0e0e0"),
            primary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            secondary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            danger = Button.default(rgb"#ffffff", rgb"#1976d2")
          ),
          input = ???
        ),
        paper = Variant(
          typography = Typography.default(rgb"#ffffff"),
          buttons = Buttons(
            normal = Button.default(rgb"#000000de", rgb"#e0e0e0"),
            primary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            secondary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            danger = Button.default(rgb"#ffffff", rgb"#1976d2")
          ),
          input = ???
        ),
        primary = Variant(
          typography = Typography.default(rgb"#ffffff"),
          buttons = Buttons(
            normal = Button.default(rgb"#000000de", rgb"#e0e0e0"),
            primary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            secondary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            danger = Button.default(rgb"#ffffff", rgb"#1976d2")
          ),
          input = ???
        ),
        secondary = Variant(
          typography = Typography.default(rgb"#ffffff"),
          buttons = Buttons(
            normal = Button.default(rgb"#000000de", rgb"#e0e0e0"),
            primary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            secondary = Button.default(rgb"#ffffff", rgb"#1976d2"),
            danger = Button.default(rgb"#ffffff", rgb"#1976d2")
          ),
          input = ???
        )
      )
    )
  }

  val Default: MaterialTheme = derive(primary = rgb"#1976d2", secondary = rgb"#1976d2")
}

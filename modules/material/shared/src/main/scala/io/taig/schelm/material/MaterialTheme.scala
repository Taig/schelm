package io.taig.schelm.material

import cats.Functor
import cats.implicits._
import io.taig.color.Color
import io.taig.color.implicits._
import io.taig.schelm.material.MaterialTheme.{Context, Mode}

final case class MaterialTheme(
    mode: MaterialTheme.Mode,
    context: MaterialTheme.Context,
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

  def modifyFlavors(f: MaterialTheme.Flavor => MaterialTheme.Flavor): MaterialTheme =
    copy(light = f(light), dark = f(dark))
}

object MaterialTheme {
  val DefaultFontFamily: String = "'Roboto', sans-serif"

  val DefaultRadius: String = "4px"

  val DefaultSpacing: Int = 8

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

  /** A color with its light and dark variants */
  final case class Shade(light: Color, normal: Color, dark: Color)

  final case class Flavor(
      palette: Palette,
      spacing: Int,
      surface: Variant,
      paper: Variant,
      primary: Variant,
      secondary: Variant
  )

  object Shade {
    def derive(color: Color): Shade = Shade(color.brighter(factor = 0.9d), color, color.darker(factor = 0.9d))
  }

  object Flavor {
    def derive(palette: Palette): Flavor = Flavor(
      palette,
      spacing = DefaultSpacing,
      surface = Variant.derive(palette, _.surface),
      paper = Variant.derive(palette, _.paper),
      primary = Variant.derive(palette, _.primary.map(_.normal)),
      secondary = Variant.derive(palette, _.secondary.map(_.normal))
    )
  }

  /** Exhaustive color palettes intended to be used when styling custom widgets
    *
    * The Palette is also used when deriving a new theme, but every widget property may be overridden separately. That
    * means that the primary color of the `Palette` won't necessarily match the primary background color of a button.
    *
    * @param primary Primary color shade, used for important interactions
    * @param secondary Secondary color shade, used for important interactions that should be distinguishable from
    *                  primary actions
    * @param surface Surface background, which is usually only applied to the page background
    * @param paper Background color for paper elements, usually white
    */
  final case class Palette(
      primary: Palette.Variant[Shade],
      secondary: Palette.Variant[Shade],
      surface: Palette.Variant[Color],
      paper: Palette.Variant[Color]
  )

  object Palette {
    final case class Variant[A](main: A, text: Color, neutral: Neutral, notification: Notification)

    object Variant {
      implicit val functor: Functor[Variant] = new Functor[Variant] {
        override def map[A, B](fa: Variant[A])(f: A => B): Variant[B] = fa.copy(main = f(fa.main))
      }
    }

    final case class Neutral(main: Shade, text: Color)

    /** Color codes for error, warning and success messages */
    final case class Notification(
        info: Notification.Variant,
        success: Notification.Variant,
        warning: Notification.Variant,
        error: Notification.Variant
    )

    object Notification {
      final case class Variant(main: Shade, text: Color)

      /** @see https://material-ui.com/customization/palette/#default-values */
      val Default: Notification = Notification(
        info = Variant(
          main = Shade(
            light = rgb"#64b5f6",
            normal = rgb"#2196f3",
            dark = rgb"#1976d2"
          ),
          text = Color.White
        ),
        success = Variant(
          main = Shade(
            light = rgb"#81c784",
            normal = rgb"#4caf50",
            dark = rgb"#388e3c"
          ),
          text = Color.White
        ),
        warning = Variant(
          main = Shade(
            light = rgb"#ffb74d",
            normal = rgb"#ff9800",
            dark = rgb"#f57c00"
          ),
          text = Color.White
        ),
        error = Variant(
          main = Shade(
            light = rgb"#e57373",
            normal = rgb"#f44336",
            dark = rgb"#d32f2f"
          ),
          text = Color.White
        )
      )
    }
  }

  final case class Variant(typography: Typography, buttons: Buttons, inputs: Inputs)

  object Variant {
    def derive(palette: Palette, select: Palette => Palette.Variant[Color]): Variant = {
      val variant = select(palette)
      val typography = Typography.derive(variant.text)

      Variant(
        typography = typography,
        buttons = Buttons(
          normal = Button.fromNeutral(variant.neutral),
          primary = Button.fromPalette(palette.primary),
          secondary = Button.fromPalette(palette.secondary),
          danger = Button.fromNotification(variant.notification.error)
        ),
        inputs = Inputs.derive(palette = variant, palette.primary.main.normal, typography)
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
    def derive(color: Color): Typography = Typography(
      h1 = Font(DefaultFontFamily, color, "6rem", "300", "6rem", "-0.015625em", None),
      h2 = Font(DefaultFontFamily, color, "3.75rem", "300", "3.75rem", "-0.0083333333em", None),
      h3 = Font(DefaultFontFamily, color, "3rem", "400", "3.125rem", "normal", None),
      h4 = Font(DefaultFontFamily, color, "2.125rem", "400", "2.5rem", "0.0073529412em", None),
      h5 = Font(DefaultFontFamily, color, "2rem", "400", "2rem", "normal", None),
      h6 = Font(DefaultFontFamily, color, "1.25rem", "500", "2rem", "0.0125rem", None),
      subtitle1 = Font(DefaultFontFamily, color, "1rem", "400", "1.75rem", "0.009375em", None),
      subtitle2 = Font(DefaultFontFamily, color, "0.875rem", "500", "1.375rem", "0.0071428571em", None),
      body1 = Font(DefaultFontFamily, color, "1rem", "400", "1.5rem", "0.03125rem", None),
      body2 = Font(DefaultFontFamily, color, "0.75rem", "400", "1.25rem", "0.0178571429em", None),
      button = Font(DefaultFontFamily, color, "0.875rem", "500", "2.25rem", "0.0892857143em", Some("uppercase")),
      caption = Font(DefaultFontFamily, color, "0.75rem", "400", "1.25rem", "0.0333333333em", None),
      overline = Font(DefaultFontFamily, color, "0.75rem", "500", "2em", "0.1666666667em", Some("uppercase"))
    )
  }

  final case class Buttons(normal: Button, primary: Button, secondary: Button, danger: Button)

  final case class Button(
      background: Color,
      spacing: Int,
      radius: String,
      font: Font,
      hover: Button.Effect,
      active: Button.Effect
  )

  object Button {
    final case class Effect(background: Color)

    def derive(text: Color, background: Color, hover: Color): Button = Button(
      background = background,
      spacing = DefaultSpacing,
      radius = DefaultRadius,
      font = Font(
        family = DefaultFontFamily,
        color = text,
        size = "0.875rem",
        weight = "500",
        lineHeight = "1.75",
        letterSpacing = "0.02857em",
        transform = Some("uppercase")
      ),
      hover = Effect(background = hover),
      active = Effect(background = hover.darker(factor = 0.8f))
    )

    def fromPalette(palette: Palette.Variant[Shade]): Button =
      derive(text = palette.text, background = palette.main.normal, hover = palette.main.dark)

    def fromNeutral(palette: Palette.Neutral): Button =
      derive(text = palette.text, background = palette.main.normal, hover = palette.main.dark)

    def fromNotification(palette: Palette.Notification.Variant): Button =
      derive(text = palette.text, background = palette.main.normal, hover = palette.main.dark)
  }

  final case class Inputs(normal: Input, warning: Input, error: Input, success: Input)

  object Inputs {
    def derive(palette: Palette.Variant[Color], primary: Color, typography: Typography): Inputs = Inputs(
      normal =
        Input.derive(palette, typography, focus = primary, hover = Some(palette.neutral.main.dark), highlight = false),
      warning = Input.fromNotification(palette, typography, notification = palette.notification.warning),
      error = Input.fromNotification(palette, typography, notification = palette.notification.error),
      success = Input.fromNotification(palette, typography, notification = palette.notification.success)
    )
  }

  final case class Input(
      spacing: Int,
      radius: String,
      background: Option[Color],
      border: Color,
      focus: Color,
      hover: Option[Color],
      value: Font,
      label: Font,
      helper: Font
  )

  object Input {
    def derive(
        palette: Palette.Variant[Color],
        typography: Typography,
        focus: Color,
        hover: Option[Color],
        highlight: Boolean
    ): Input = {
      val caption = if (highlight) typography.caption.copy(color = focus) else typography.caption

      Input(
        spacing = DefaultSpacing,
        radius = DefaultRadius,
        background = None,
        border = if (highlight) focus else palette.neutral.main.normal,
        focus,
        hover,
        value = typography.body1.copy(color = palette.text),
        label = caption,
        helper = caption
      )
    }

    def fromNotification(
        palette: Palette.Variant[Color],
        typography: Typography,
        notification: Palette.Notification.Variant
    ): Input =
      derive(palette, typography, hover = None, focus = notification.main.normal, highlight = true)
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

  def derive(primary: Color, secondary: Color): MaterialTheme = {
    val light = Palette(
      primary = Palette.Variant(
        main = Shade.derive(primary),
        text = rgb"#ffffff",
        neutral = Palette.Neutral(main = Shade.derive(rgb"#cbcbcb"), text = rgb"#000000de"),
        notification = Palette.Notification.Default
      ),
      secondary = Palette.Variant(
        main = Shade.derive(secondary),
        text = rgb"#ffffff",
        neutral = Palette.Neutral(main = Shade.derive(rgb"#cbcbcb"), text = rgb"#000000de"),
        notification = Palette.Notification.Default
      ),
      surface = Palette.Variant(
        main = rgb"#f7f7f7",
        text = rgb"#000000de",
        neutral = Palette.Neutral(main = Shade.derive(rgb"#cbcbcb"), text = rgb"#000000de"),
        notification = Palette.Notification.Default
      ),
      paper = Palette.Variant(
        main = rgb"#ffffff",
        text = rgb"#000000de",
        neutral = Palette.Neutral(main = Shade.derive(rgb"#cbcbcb"), text = rgb"#000000de"),
        notification = Palette.Notification.Default
      )
    )

    val dark = Palette(
      primary = Palette.Variant(
        main = Shade.derive(primary),
        text = rgb"#ffffff",
        neutral = Palette.Neutral(main = Shade.derive(rgb"#e0e0e0"), text = rgb"#000000de"),
        notification = Palette.Notification.Default
      ),
      secondary = Palette.Variant(
        main = Shade.derive(secondary),
        text = rgb"#ffffff",
        neutral = Palette.Neutral(main = Shade.derive(rgb"#e0e0e0"), text = rgb"#000000de"),
        notification = Palette.Notification.Default
      ),
      surface = Palette.Variant(
        main = rgb"#121212",
        text = rgb"#ffffff",
        neutral = Palette.Neutral(main = Shade.derive(rgb"#e0e0e0"), text = rgb"#000000de"),
        notification = Palette.Notification.Default
      ),
      paper = Palette.Variant(
        main = rgb"#333333",
        text = rgb"#ffffff",
        neutral = Palette.Neutral(main = Shade.derive(rgb"#e0e0e0"), text = rgb"#000000de"),
        notification = Palette.Notification.Default
      )
    )

    MaterialTheme(
      mode = Mode.Light,
      context = Context.Surface,
      light = Flavor.derive(light),
      dark = Flavor.derive(dark)
    )
  }

  val Default: MaterialTheme = derive(primary = rgb"#1976d2", secondary = rgb"#1976d2")
}

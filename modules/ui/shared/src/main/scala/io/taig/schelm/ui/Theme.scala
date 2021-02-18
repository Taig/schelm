package io.taig.schelm.ui

import cats.Functor
import cats.syntax.all._
import io.taig.color.Color
import io.taig.color.implicits._
import io.taig.schelm.dsl._

final case class Theme(context: Theme.Context, variants: Theme.Variants) {
  def variant: Theme.Variant[Color] = variants(context)
}

object Theme {
  val DefaultFontFamily: String = "'Roboto', sans-serif"

  val DefaultRadius: LengthUnit = 4.px

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

  final case class Variants(
      surface: Variant[Color],
      paper: Variant[Color],
      primary: Variant[Shade],
      secondary: Variant[Shade]
  ) {
    def apply(context: Context): Variant[Color] = context match {
      case Context.Surface   => surface
      case Context.Paper     => paper
      case Context.Primary   => primary.map(_.normal)
      case Context.Secondary => secondary.map(_.normal)
    }
  }

  final case class Variant[A](palette: Palette[A], typography: Theme.Typography) {
    def map[B](f: A => B): Variant[B] = copy(palette = palette.map(f))
  }

  object Variant {
    implicit val functor: Functor[Variant] = new Functor[Variant] {
      override def map[A, B](fa: Variant[A])(f: A => B): Variant[B] = fa.map(f)
    }
  }

  final case class Palette[A](main: A, neutral: Shade, notifications: Notifications) {
    def map[B](f: A => B): Palette[B] = copy(main = f(main))
  }

  object Palette {
    def deriveColor(main: Color): Palette[Color] = Palette(
      main,
      neutral = Shade.derive(hex"#cbcbcb"),
      notifications = Notifications.Default
    )

    def deriveShade(main: Color): Palette[Shade] = deriveColor(main).as(Shade.derive(main))

    implicit val functor: Functor[Palette] = new Functor[Palette] {
      override def map[A, B](fa: Palette[A])(f: A => B): Palette[B] = fa.map(f)
    }
  }

  /** A color with its light and dark variants */
  final case class Shade(light: Color, normal: Color, dark: Color)

  object Shade {
    def derive(color: Color): Shade = Shade(color.brighter(factor = 0.8), color, color.darker(factor = 0.8))
  }

  final case class Notifications(info: Notification, success: Notification, warning: Notification, error: Notification)

  object Notifications {

    /** @see https://material-ui.com/customization/palette/#default-values */
    val Default: Notifications = Notifications(
      info = Notification(
        main = Shade(light = hex"#64b5f6", normal = hex"#2196f3", dark = hex"#1976d2"),
        text = Color.White
      ),
      success = Notification(
        main = Shade(light = hex"#81c784", normal = hex"#4caf50", dark = hex"#388e3c"),
        text = Color.White
      ),
      warning = Notification(
        main = Shade(light = hex"#ffb74d", normal = hex"#ff9800", dark = hex"#f57c00"),
        text = Color.White
      ),
      error = Notification(
        main = Shade(light = hex"#e57373", normal = hex"#f44336", dark = hex"#d32f2f"),
        text = Color.White
      )
    )
  }

  final case class Notification(main: Shade, text: Color)

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
      h1 = Font(DefaultFontFamily, color, 6.rem, "300", 6.rem, -0.015625f.em.some, None),
      h2 = Font(DefaultFontFamily, color, 3.75f.rem, "300", 3.75f.rem, -0.0083333333f.em.some, None),
      h3 = Font(DefaultFontFamily, color, 3.rem, "400", 3.125f.rem, None, None),
      h4 = Font(DefaultFontFamily, color, 2.125f.rem, "400", 2.5f.rem, 0.0073529412f.em.some, None),
      h5 = Font(DefaultFontFamily, color, 2.rem, "400", 2.rem, None, None),
      h6 = Font(DefaultFontFamily, color, 1.25f.rem, "500", 2.rem, 0.0125f.rem.some, None),
      subtitle1 = Font(DefaultFontFamily, color, 1.rem, "400", 1.75f.rem, 0.009375f.em.some, None),
      subtitle2 = Font(DefaultFontFamily, color, 0.875f.rem, "500", 1.375f.rem, 0.0071428571f.em.some, None),
      body1 = Font(DefaultFontFamily, color, 1.rem, "400", 1.5f.rem, 0.03125f.rem.some, None),
      body2 = Font(DefaultFontFamily, color, 0.75f.rem, "400", 1.25f.rem, 0.0178571429f.em.some, None),
      button = Font(DefaultFontFamily, color, 0.875f.rem, "500", 2.25f.rem, 0.0892857143f.em.some, Some("uppercase")),
      caption = Font(DefaultFontFamily, color, 0.75f.rem, "400", 1.25f.rem, 0.0333333333f.em.some, None),
      overline = Font(DefaultFontFamily, color, 0.75f.rem, "500", 2.em, 0.1666666667f.em.some, Some("uppercase"))
    )
  }

  final case class Font(
      family: String,
      color: Color,
      size: LengthUnit,
      weight: String,
      lineHeight: LengthUnit,
      letterSpacing: Option[LengthUnit],
      transform: Option[String]
  )

  def derive(primary: Color, secondary: Color, mode: Mode): Theme = {
    val variants = mode match {
      case Mode.Light =>
        Variants(
          surface = Variant(
            palette = Palette.deriveColor(hex"#f7f7f7"),
            typography = Typography.derive(hex"#000000de")
          ),
          paper = Variant(
            palette = Palette.deriveColor(hex"#ffffff"),
            typography = Typography.derive(hex"#000000de")
          ),
          primary = Variant(
            palette = Palette.deriveShade(primary),
            typography = Typography.derive(hex"#000000de")
          ),
          secondary = Variant(
            palette = Palette.deriveShade(secondary),
            typography = Typography.derive(hex"#000000de")
          )
        )
      case Mode.Dark => ???
    }

    Theme(Context.Surface, variants)
  }

  val Default: Theme = derive(primary = hex"#1976d2", secondary = hex"#1976d2", Mode.Light)
}

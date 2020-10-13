package io.taig.schelm.mdc

import io.taig.schelm.mdc.MdcTheme.Text

final case class MdcTheme(palette: MdcTheme.Palette, text: MdcTheme.Text) {
  def modifyPalette(f: MdcTheme.Palette => MdcTheme.Palette): MdcTheme = copy(palette = f(palette))

  def modifyText(f: MdcTheme.Text => MdcTheme.Text): MdcTheme = copy(text = f(text))
}

object MdcTheme {
  sealed abstract class Mode extends Product with Serializable

  object Mode {
    final case object Dark extends Mode
    final case object Light extends Mode
  }

  final case class Palette(primary: String)

  final case class Text(
      headline1: Text.Font,
      headline2: Text.Font,
      headline6: Text.Font,
      body1: Text.Font
  ) {
    def apply(variant: Text.Variant): Text.Font = variant match {
      case Text.Variant.Headline1 => headline1
      case Text.Variant.Headline2 => headline2
      case Text.Variant.Headline6 => headline6
      case Text.Variant.Body1     => body1
    }
  }

  object Text {
    final case class Font(
        family: String,
        size: String,
        weight: String,
        lineHeight: String,
        letterSpacing: String,
        decoration: Option[String],
        transform: Option[String],
        palette: Palette
    )

    /** @param main Text color on generic surfaces
      * @param primary Text color that on primary colored surfaces
      */
    final case class Palette(main: String, primary: String)

    object Palette {
      val Light: Palette = Palette(main = "rgba(0, 0, 0, 0.87)", primary = "#ffffff")

      val Dark: Palette = Text.Palette(main = "#ffffff", primary = "#ffffff")
    }

    sealed abstract class Variant extends Product with Serializable

    object Variant {
      final case object Headline1 extends Variant
      final case object Headline2 extends Variant
      final case object Headline3 extends Variant
      final case object Headline4 extends Variant
      final case object Headline5 extends Variant
      final case object Headline6 extends Variant
      final case object Subtitle1 extends Variant
      final case object Subtitle2 extends Variant
      final case object Body1 extends Variant
      final case object Body2 extends Variant
      final case object Caption extends Variant
      final case object Button extends Variant
      final case object Overline extends Variant
    }

    val DefaultFontFamily: String = "'Roboto', sans-serif"
  }

  val Light: MdcTheme = MdcTheme(
    palette = Palette(primary = "#1976d2"),
    text = Text(
      headline1 = Text.Font(
        family = Text.DefaultFontFamily,
        size = "6rem",
        weight = "300",
        lineHeight = "6rem",
        letterSpacing = "-0.015625em",
        decoration = None,
        transform = None,
        palette = Text.Palette.Light
      ),
      headline2 = Text.Font(
        family = Text.DefaultFontFamily,
        size = "3.75rem",
        weight = "300",
        lineHeight = "3.75rem",
        letterSpacing = "-0.0083333333em",
        decoration = None,
        transform = None,
        palette = Text.Palette.Light
      ),
      headline6 = Text.Font(
        family = Text.DefaultFontFamily,
        size = "3.75rem",
        weight = "300",
        lineHeight = "3.75rem",
        letterSpacing = "-0.0083333333em",
        decoration = None,
        transform = None,
        palette = Text.Palette.Light
      ),
      body1 = Text.Font(
        family = Text.DefaultFontFamily,
        size = "3.75rem",
        weight = "300",
        lineHeight = "3.75rem",
        letterSpacing = "-0.0083333333em",
        decoration = None,
        transform = None,
        palette = Text.Palette.Light
      )
    )
  )

  val Dark: MdcTheme = MdcTheme(
    palette = Palette(primary = "#333333"),
    text = Text(
      headline1 = Text.Font(
        family = Text.DefaultFontFamily,
        size = "6rem",
        weight = "300",
        lineHeight = "6rem",
        letterSpacing = "-0.015625em",
        decoration = None,
        transform = None,
        palette = Text.Palette.Dark
      ),
      headline2 = Text.Font(
        family = Text.DefaultFontFamily,
        size = "3.75rem",
        weight = "300",
        lineHeight = "3.75rem",
        letterSpacing = "-0.0083333333em",
        decoration = None,
        transform = None,
        palette = Text.Palette.Dark
      ),
      headline6 = Text.Font(
        family = Text.DefaultFontFamily,
        size = "3.75rem",
        weight = "300",
        lineHeight = "3.75rem",
        letterSpacing = "-0.0083333333em",
        decoration = None,
        transform = None,
        palette = Text.Palette.Light
      ),
      body1 = Text.Font(
        family = Text.DefaultFontFamily,
        size = "3.75rem",
        weight = "300",
        lineHeight = "3.75rem",
        letterSpacing = "-0.0083333333em",
        decoration = None,
        transform = None,
        palette = Text.Palette.Dark
      )
    )
  )
}

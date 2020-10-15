package io.taig.schelm.mdc

import io.taig.color.Color
import io.taig.color.implicits._
import io.taig.schelm.mdc.MdcTheme.Text.Variant
import io.taig.schelm.mdc.MdcTheme.{Context, Mode}

final case class MdcTheme(mode: MdcTheme.Mode, context: MdcTheme.Context, light: MdcTheme.Rules, dark: MdcTheme.Rules) {
  def rules: MdcTheme.Rules = mode match {
    case Mode.Dark  => dark
    case Mode.Light => light
  }

  def rule: MdcTheme.Rule = context match {
    case Context.Paper     => rules.paper
    case Context.Primary   => rules.primary
    case Context.Secondary => rules.secondary
    case Context.Surface   => rules.surface
  }
}

object MdcTheme {
  sealed abstract class Mode extends Product with Serializable

  object Mode {
    final case object Dark extends Mode
    final case object Light extends Mode

    val Default: Mode = Light
  }

  sealed abstract class Context extends Product with Serializable

  object Context {
    final case object Paper extends Context
    final case object Primary extends Context
    final case object Secondary extends Context
    final case object Surface extends Context

    val Default: Context = Surface
  }

  final case class Rules(paper: Rule, primary: Rule, secondary: Rule, surface: Rule)

  final case class Rule(color: Color, text: Text)

  final case class Text(
      body1: Text.Font,
      body2: Text.Font,
      button: Text.Font,
      caption: Text.Font,
      headline1: Text.Font,
      headline2: Text.Font,
      headline3: Text.Font,
      headline4: Text.Font,
      headline5: Text.Font,
      headline6: Text.Font,
      overline: Text.Font,
      subtitle1: Text.Font,
      subtitle2: Text.Font
  ) {
    def apply(variant: Text.Variant): Text.Font = variant match {
      case Variant.Body1     => body1
      case Variant.Body2     => body2
      case Variant.Button    => button
      case Variant.Caption   => caption
      case Variant.Headline1 => headline1
      case Variant.Headline2 => headline2
      case Variant.Headline3 => headline3
      case Variant.Headline4 => headline4
      case Variant.Headline5 => headline5
      case Variant.Headline6 => headline6
      case Variant.Overline  => overline
      case Variant.Subtitle1 => subtitle1
      case Variant.Subtitle2 => subtitle2
    }
  }

  object Text {
    sealed abstract class Variant extends Product with Serializable

    object Variant {
      final case object Body1 extends Variant
      final case object Body2 extends Variant
      final case object Button extends Variant
      final case object Caption extends Variant
      final case object Headline1 extends Variant
      final case object Headline2 extends Variant
      final case object Headline3 extends Variant
      final case object Headline4 extends Variant
      final case object Headline5 extends Variant
      final case object Headline6 extends Variant
      final case object Overline extends Variant
      final case object Subtitle1 extends Variant
      final case object Subtitle2 extends Variant
    }

    final case class Font(
        family: String,
        color: Color,
        size: String,
        weight: String,
        lineHeight: String,
        letterSpacing: String,
        decoration: Option[String],
        transform: Option[String]
    )

    val DefaultFontFamily: String = "'Roboto', sans-serif"

    val Fallback = Text.Font(
      family = Text.DefaultFontFamily,
      color = rgb"#000000",
      size = "3.75rem",
      weight = "300",
      lineHeight = "3.75rem",
      letterSpacing = "-0.0083333333em",
      decoration = None,
      transform = None
    )

    def default(color: Color): Text = Text(
      body1 = Text.Font(
        family = Text.DefaultFontFamily,
        color = color,
        size = "3.75rem",
        weight = "300",
        lineHeight = "3.75rem",
        letterSpacing = "-0.0083333333em",
        decoration = None,
        transform = None
      ),
      body2 = Fallback,
      button = Fallback,
      caption = Fallback,
      headline1 = Text.Font(
        family = Text.DefaultFontFamily,
        color = color,
        size = "6rem",
        weight = "300",
        lineHeight = "6rem",
        letterSpacing = "-0.015625em",
        decoration = None,
        transform = None
      ),
      headline2 = Text.Font(
        family = Text.DefaultFontFamily,
        color = color,
        size = "3.75rem",
        weight = "300",
        lineHeight = "3.75rem",
        letterSpacing = "-0.0083333333em",
        decoration = None,
        transform = None
      ),
      headline3 = Fallback,
      headline4 = Fallback,
      headline5 = Fallback,
      headline6 = Text.Font(
        family = Text.DefaultFontFamily,
        color = color,
        size = "3.75rem",
        weight = "300",
        lineHeight = "3.75rem",
        letterSpacing = "-0.0083333333em",
        decoration = None,
        transform = None
      ),
      overline = Fallback,
      subtitle1 = Fallback,
      subtitle2 = Fallback
    )
  }

  val Default: MdcTheme = MdcTheme(
    mode = Mode.Default,
    context = Context.Default,
    light = Rules(
      paper = Rule(color = rgb"#ffffff", text = Text.default(rgb"#000000de")),
      primary = Rule(color = rgb"#6200ee", text = Text.default(rgb"#ffffff")),
      secondary = Rule(color = rgb"#018786", text = Text.default(rgb"#ffffff")),
      surface = Rule(color = rgb"#f7f7f7", text = Text.default(rgb"#000000de"))
    ),
    dark = Rules(
      paper = Rule(color = rgb"#333333", text = Text.default(rgb"#ffffff")),
      primary = Rule(color = rgb"#9d57ff", text = Text.default(rgb"#000000")),
      secondary = Rule(color = rgb"#01efef", text = Text.default(rgb"#000000")),
      surface = Rule(color = rgb"#212121", text = Text.default(rgb"#ffffff"))
    )
  )
}

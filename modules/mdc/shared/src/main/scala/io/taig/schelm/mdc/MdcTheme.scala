package io.taig.schelm.mdc

import io.taig.schelm.mdc.MdcTheme.Mode

final case class MdcTheme(mode: MdcTheme.Mode, light: MdcTheme.Variant, dark: MdcTheme.Variant, fontFamily: String) {
  def variant: MdcTheme.Variant = mode match {
    case Mode.Dark  => dark
    case Mode.Light => light
  }

  def toggle: MdcTheme = mode match {
    case Mode.Dark  => copy(mode = Mode.Light)
    case Mode.Light => copy(mode = Mode.Dark)
  }
}

object MdcTheme {
  sealed abstract class Mode extends Product with Serializable

  object Mode {
    final case object Dark extends Mode
    final case object Light extends Mode
  }

  final case class Variant(palette: Palette, text: Text)

  final case class Palette(primary: String)

  final case class Text(main: String, primary: String)

  val Default: MdcTheme = MdcTheme(
    mode = Mode.Light,
    light = Variant(
      palette = Palette(primary = "#1976d2"),
      text = Text(main = "rgba(0, 0, 0, 0.87)", primary = "#ffffff")
    ),
    dark = Variant(
      palette = Palette(primary = "#333333"),
      text = Text(main = "#ffffff", primary = "#ffffff")
    ),
    fontFamily = "'Roboto', sans-serif"
  )
}

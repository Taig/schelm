package io.taig.schelm.ui

import io.taig.schelm.css.data.Style
import io.taig.schelm.data.Tag
import io.taig.schelm.dsl._

object Typography {
  private val fontSmoothing: Style = styles(
    webkit("font-smoothing") := "antialiased",
    moz("osx-font-smoothing") := "grayscale"
  )

//  def apply(name: Tag.Name, value: String): Widget[Nothing, Nothing, Theme.Font] = contextual { theme =>
//    val style = styles(
//      color := theme.color,
//      fontFamily := theme.family,
//      fontSize := theme.size,
//      fontWeight := theme.weight,
//      letterSpacing := theme.letterSpacing,
//      lineHeight := theme.lineHeight,
//      textTransform := theme.transform
//    ) ++ fontSmoothing
//
//    tag(name).css
//      .set(style)
//      .children(text(value))
//  }
//
//  def h1(value: String, name: Tag.Name = H1): Widget[Nothing, Nothing, Theme] =
//    Typography(name, value).select(_.variant.typography.h1)
//
//  def h2(value: String, name: Tag.Name = H2): Widget[Nothing, Nothing, Theme] =
//    Typography(name, value).select(_.variant.typography.h2)
//
//  def h3(value: String, name: Tag.Name = H3): Widget[Nothing, Nothing, Theme] =
//    Typography(name, value).select(_.variant.typography.h3)
//
//  def h4(value: String, name: Tag.Name = H4): Widget[Nothing, Nothing, Theme] =
//    Typography(name, value).select(_.variant.typography.h4)
//
//  def h5(value: String, name: Tag.Name = H5): Widget[Nothing, Nothing, Theme] =
//    Typography(name, value).select(_.variant.typography.h5)
//
//  def h6(value: String, name: Tag.Name = H6): Widget[Nothing, Nothing, Theme] =
//    Typography(name, value).select(_.variant.typography.h6)
//
//  def subtitle1(value: String, name: Tag.Name = H6): Widget[Nothing, Nothing, Theme] =
//    Typography(name, value).select(_.variant.typography.subtitle1)
//
//  def subtitle2(value: String, name: Tag.Name = H6): Widget[Nothing, Nothing, Theme] =
//    Typography(name, value).select(_.variant.typography.subtitle2)
//
//  def body1(value: String, name: Tag.Name = P): Widget[Nothing, Nothing, Theme] =
//    Typography(name, value).select(_.variant.typography.body1)
//
//  def body2(value: String, name: Tag.Name = P): Widget[Nothing, Nothing, Theme] =
//    Typography(name, value).select(_.variant.typography.body2)
//
//  def button(value: String, name: Tag.Name = Span): Widget[Nothing, Nothing, Theme] =
//    Typography(name, value).select(_.variant.typography.button)
//
//  def caption(value: String, name: Tag.Name = Span): Widget[Nothing, Nothing, Theme] =
//    Typography(name, value).select(_.variant.typography.caption)
//
//  def overline(value: String, name: Tag.Name = Span): Widget[Nothing, Nothing, Theme] =
//    Typography(name, value).select(_.variant.typography.overline)
}

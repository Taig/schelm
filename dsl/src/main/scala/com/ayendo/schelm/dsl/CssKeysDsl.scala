package com.ayendo.schelm.dsl

import com.ayendo.schelm.css.Declaration

trait CssKeysDsl {
  def declaration(key: String, value: String): Declaration =
    Declaration(key, value)

  def borderBottom(value: String): Declaration =
    declaration("border-bottom", value)

  def borderStyle(value: String): Declaration =
    declaration("border-style", value)

  def bottom(value: String): Declaration = declaration("bottom", value)

  def boxSizing(value: String): Declaration = declaration("box-sizing", value)

  def color(value: String): Declaration = declaration("color", value)

  def cursor(value: String): Declaration = declaration("cursor", value)

  def display(value: String): Declaration = declaration("display", value)

  def backgroundColor(value: String): Declaration =
    declaration("background-color", value)

  def content(value: String): Declaration = declaration("content", value)

  def font(value: String): Declaration = declaration("font", value)

  def fontFamily(value: String): Declaration = declaration("font-family", value)

  def fontSize(value: String): Declaration = declaration("font-size", value)

  def fontWeight(value: String): Declaration = declaration("font-weight", value)

  def height(value: String): Declaration = declaration("height", value)

  def left(value: String): Declaration = declaration("left", value)

  def lineHeight(value: String): Declaration =
    declaration("line-height", value)

  def margin(value: String): Declaration = declaration("margin", value)

  def maxWidth(value: String): Declaration = declaration("max-width", value)

  def minWidth(value: String): Declaration = declaration("min-width", value)

  def outline(value: String): Declaration = declaration("outline", value)

  def outlineOffset(value: String): Declaration =
    declaration("outline-offset", value)

  def overflow(value: String): Declaration = declaration("overflow", value)

  def padding(value: String): Declaration = declaration("padding", value)

  def position(value: String): Declaration = declaration("position", value)

  def right(value: String): Declaration = declaration("right", value)

  def textDecoration(value: String): Declaration =
    declaration("text-decoration", value)

  def textTransform(value: String): Declaration =
    declaration("text-transform", value)

  def top(value: String): Declaration = declaration("top", value)

  def verticalAlign(value: String): Declaration =
    declaration("vertical-align", value)

  def whiteSpace(value: String): Declaration = declaration("white-space", value)

  def width(value: String): Declaration = declaration("width", value)
}

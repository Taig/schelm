package io.taig.schelm.dsl

import io.taig.schelm.css.Declaration

trait CssKeysDsl {
  def declaration(key: String, value: String): Declaration =
    Declaration(key, value)

  def alignContent(value: String): Declaration =
    declaration("align-content", value)
  def alignItems(value: String): Declaration = declaration("align-items", value)
  def alignSelf(value: String): Declaration = declaration("align-self", value)
  def animation(value: String): Declaration = declaration("animation", value)
  def animationDelay(value: String): Declaration =
    declaration("animation-delay", value)
  def animationDirection(value: String): Declaration =
    declaration("animation-direction", value)
  def animationDuration(value: String): Declaration =
    declaration("animation-duration", value)
  def animationFillMode(value: String): Declaration =
    declaration("animation-fill-mode", value)
  def animationIterationCount(value: String): Declaration =
    declaration("animation-iteration-count", value)
  def animationName(value: String): Declaration =
    declaration("animation-name", value)
  def animationPlayState(value: String): Declaration =
    declaration("animation-play-state", value)
  def animationTimingFunction(value: String): Declaration =
    declaration("animation-timing-function", value)
  def backfaceVisibility(value: String): Declaration =
    declaration("backface-visibility", value)
  def background(value: String): Declaration = declaration("background", value)
  def backgroundAttachment(value: String): Declaration =
    declaration("background-attachment", value)
  def backgroundClip(value: String): Declaration =
    declaration("background-clip", value)
  def backgroundColor(value: String): Declaration =
    declaration("background-color", value)
  def backgroundImage(value: String): Declaration =
    declaration("background-image", value)
  def backgroundOrigin(value: String): Declaration =
    declaration("background-origin", value)
  def backgroundPosition(value: String): Declaration =
    declaration("background-position", value)
  def backgroundRepeat(value: String): Declaration =
    declaration("background-repeat", value)
  def backgroundSize(value: String): Declaration =
    declaration("background-size", value)
  def border(value: String): Declaration = declaration("border", value)
  def borderBottom(value: String): Declaration =
    declaration("border-bottom", value)
  def borderBottomColor(value: String): Declaration =
    declaration("border-bottom-color", value)
  def borderBottomLeftRadius(value: String): Declaration =
    declaration("border-bottom-left-radius", value)
  def borderBottomRightRadius(value: String): Declaration =
    declaration("border-bottom-right-radius", value)
  def borderBottomStyle(value: String): Declaration =
    declaration("border-bottom-style", value)
  def borderBottomWidth(value: String): Declaration =
    declaration("border-bottom-width", value)
  def borderCollapse(value: String): Declaration =
    declaration("border-collapse", value)
  def borderColor(value: String): Declaration =
    declaration("border-color", value)
  def borderImage(value: String): Declaration =
    declaration("border-image", value)
  def borderImageOutset(value: String): Declaration =
    declaration("border-image-outset", value)
  def borderImageRepeat(value: String): Declaration =
    declaration("border-image-repeat", value)
  def borderImageSlice(value: String): Declaration =
    declaration("border-image-slice", value)
  def borderImageSource(value: String): Declaration =
    declaration("border-image-source", value)
  def borderImageWidth(value: String): Declaration =
    declaration("border-image-width", value)
  def borderLeft(value: String): Declaration = declaration("border-left", value)
  def borderLeftColor(value: String): Declaration =
    declaration("border-left-color", value)
  def borderLeftStyle(value: String): Declaration =
    declaration("border-left-style", value)
  def borderLeftWidth(value: String): Declaration =
    declaration("border-left-width", value)
  def borderRadius(value: String): Declaration =
    declaration("border-radius", value)
  def borderRight(value: String): Declaration =
    declaration("border-right", value)
  def borderRightColor(value: String): Declaration =
    declaration("border-right-color", value)
  def borderRightStyle(value: String): Declaration =
    declaration("border-right-style", value)
  def borderRightWidth(value: String): Declaration =
    declaration("border-right-width", value)
  def borderSpacing(value: String): Declaration =
    declaration("border-spacing", value)
  def borderStyle(value: String): Declaration =
    declaration("border-style", value)
  def borderTop(value: String): Declaration = declaration("border-top", value)
  def borderTopColor(value: String): Declaration =
    declaration("border-top-color", value)
  def borderTopLeftRadius(value: String): Declaration =
    declaration("border-top-left-radius", value)
  def borderTopRightRadius(value: String): Declaration =
    declaration("border-top-right-radius", value)
  def borderTopStyle(value: String): Declaration =
    declaration("border-top-style", value)
  def borderTopWidth(value: String): Declaration =
    declaration("border-top-width", value)
  def borderWidth(value: String): Declaration =
    declaration("border-width", value)
  def bottom(value: String): Declaration = declaration("bottom", value)
  def boxShadow(value: String): Declaration = declaration("box-shadow", value)
  def boxSizing(value: String): Declaration = declaration("box-sizing", value)
  def captionSide(value: String): Declaration =
    declaration("caption-side", value)
  def clear(value: String): Declaration = declaration("clear", value)
  def clip(value: String): Declaration = declaration("clip", value)
  def color(value: String): Declaration = declaration("color", value)
  def columnCount(value: String): Declaration =
    declaration("column-count", value)
  def columnFill(value: String): Declaration = declaration("column-fill", value)
  def columnGap(value: String): Declaration = declaration("column-gap", value)
  def columnRule(value: String): Declaration = declaration("column-rule", value)
  def columnRuleColor(value: String): Declaration =
    declaration("column-rule-color", value)
  def columnRuleStyle(value: String): Declaration =
    declaration("column-rule-style", value)
  def columnRuleWidth(value: String): Declaration =
    declaration("column-rule-width", value)
  def columnSpan(value: String): Declaration = declaration("column-span", value)
  def columnWidth(value: String): Declaration =
    declaration("column-width", value)
  def columns(value: String): Declaration = declaration("columns", value)
  def content(value: String): Declaration = declaration("content", value)
  def counterIncrement(value: String): Declaration =
    declaration("counter-increment", value)
  def counterReset(value: String): Declaration =
    declaration("counter-reset", value)
  def cursor(value: String): Declaration = declaration("cursor", value)
  def direction(value: String): Declaration = declaration("direction", value)
  def display(value: String): Declaration = declaration("display", value)
  def emptyCells(value: String): Declaration = declaration("empty-cells", value)
  def flex(value: String): Declaration = declaration("flex", value)
  def flexBasis(value: String): Declaration = declaration("flex-basis", value)
  def flexDirection(value: String): Declaration =
    declaration("flex-direction", value)
  def flexFlow(value: String): Declaration = declaration("flex-flow", value)
  def flexGrow(value: String): Declaration = declaration("flex-grow", value)
  def flexShrink(value: String): Declaration = declaration("flex-shrink", value)
  def flexWrap(value: String): Declaration = declaration("flex-wrap", value)
  def float(value: String): Declaration = declaration("float", value)
  def font(value: String): Declaration = declaration("font", value)
  def fontFamily(value: String): Declaration = declaration("font-family", value)
  def fontSize(value: String): Declaration = declaration("font-size", value)
  def fontSizeAdjust(value: String): Declaration =
    declaration("font-size-adjust", value)
  def fontStretch(value: String): Declaration =
    declaration("font-stretch", value)
  def fontStyle(value: String): Declaration = declaration("font-style", value)
  def fontVariant(value: String): Declaration =
    declaration("font-variant", value)
  def fontWeight(value: String): Declaration = declaration("font-weight", value)
  def height(value: String): Declaration = declaration("height", value)
  def justifyContent(value: String): Declaration =
    declaration("justify-content", value)
  def left(value: String): Declaration = declaration("left", value)
  def letterSpacing(value: String): Declaration =
    declaration("letter-spacing", value)
  def lineHeight(value: String): Declaration = declaration("line-height", value)
  def listStyle(value: String): Declaration = declaration("list-style", value)
  def listStyleImage(value: String): Declaration =
    declaration("list-style-image", value)
  def listStylePosition(value: String): Declaration =
    declaration("list-style-position", value)
  def listStyleType(value: String): Declaration =
    declaration("list-style-type", value)
  def margin(value: String): Declaration = declaration("margin", value)
  def marginBottom(value: String): Declaration =
    declaration("margin-bottom", value)
  def marginLeft(value: String): Declaration = declaration("margin-left", value)
  def marginRight(value: String): Declaration =
    declaration("margin-right", value)
  def marginTop(value: String): Declaration = declaration("margin-top", value)
  def maxHeight(value: String): Declaration = declaration("max-height", value)
  def maxWidth(value: String): Declaration = declaration("max-width", value)
  def minHeight(value: String): Declaration = declaration("min-height", value)
  def minWidth(value: String): Declaration = declaration("min-width", value)
  def opacity(value: String): Declaration = declaration("opacity", value)
  def order(value: String): Declaration = declaration("order", value)
  def outline(value: String): Declaration = declaration("outline", value)
  def outlineColor(value: String): Declaration =
    declaration("outline-color", value)
  def outlineOffset(value: String): Declaration =
    declaration("outline-offset", value)
  def outlineStyle(value: String): Declaration =
    declaration("outline-style", value)
  def outlineWidth(value: String): Declaration =
    declaration("outline-width", value)
  def overflow(value: String): Declaration = declaration("overflow", value)
  def overflowX(value: String): Declaration = declaration("overflow-x", value)
  def overflowY(value: String): Declaration = declaration("overflow-y", value)
  def padding(value: String): Declaration = declaration("padding", value)
  def paddingBottom(value: String): Declaration =
    declaration("padding-bottom", value)
  def paddingLeft(value: String): Declaration =
    declaration("padding-left", value)
  def paddingRight(value: String): Declaration =
    declaration("padding-right", value)
  def paddingTop(value: String): Declaration = declaration("padding-top", value)
  def pageBreakAfter(value: String): Declaration =
    declaration("page-break-after", value)
  def pageBreakBefore(value: String): Declaration =
    declaration("page-break-before", value)
  def pageBreakInside(value: String): Declaration =
    declaration("page-break-inside", value)
  def perspective(value: String): Declaration =
    declaration("perspective", value)
  def perspectiveOrigin(value: String): Declaration =
    declaration("perspective-origin", value)
  def position(value: String): Declaration = declaration("position", value)
  def quotes(value: String): Declaration = declaration("quotes", value)
  def resize(value: String): Declaration = declaration("resize", value)
  def right(value: String): Declaration = declaration("right", value)
  def tabSize(value: String): Declaration = declaration("tab-size", value)
  def tableLayout(value: String): Declaration =
    declaration("table-layout", value)
  def textAlign(value: String): Declaration = declaration("text-align", value)
  def textAlignLast(value: String): Declaration =
    declaration("text-align-last", value)
  def textDecoration(value: String): Declaration =
    declaration("text-decoration", value)
  def textDecorationColor(value: String): Declaration =
    declaration("text-decoration-color", value)
  def textDecorationLine(value: String): Declaration =
    declaration("text-decoration-line", value)
  def textDecorationStyle(value: String): Declaration =
    declaration("text-decoration-style", value)
  def textIndent(value: String): Declaration = declaration("text-indent", value)
  def textJustify(value: String): Declaration =
    declaration("text-justify", value)
  def textOverflow(value: String): Declaration =
    declaration("text-overflow", value)
  def textShadow(value: String): Declaration = declaration("text-shadow", value)
  def textTransform(value: String): Declaration =
    declaration("text-transform", value)
  def top(value: String): Declaration = declaration("top", value)
  def transform(value: String): Declaration = declaration("transform", value)
  def transformOrigin(value: String): Declaration =
    declaration("transform-origin", value)
  def transformStyle(value: String): Declaration =
    declaration("transform-style", value)
  def transition(value: String): Declaration = declaration("transition", value)
  def transitionDelay(value: String): Declaration =
    declaration("transition-delay", value)
  def transitionDuration(value: String): Declaration =
    declaration("transition-duration", value)
  def transitionProperty(value: String): Declaration =
    declaration("transition-property", value)
  def transitionTimingFunction(value: String): Declaration =
    declaration("transition-timing-function", value)
  def verticalAlign(value: String): Declaration =
    declaration("vertical-align", value)
  def visibility(value: String): Declaration = declaration("visibility", value)
  def whiteSpace(value: String): Declaration = declaration("white-space", value)
  def width(value: String): Declaration = declaration("width", value)
  def wordBreak(value: String): Declaration = declaration("word-break", value)
  def wordSpacing(value: String): Declaration =
    declaration("word-spacing", value)
  def wordWrap(value: String): Declaration = declaration("word-wrap", value)
  def zIndex(value: String): Declaration = declaration("z-index", value)
}

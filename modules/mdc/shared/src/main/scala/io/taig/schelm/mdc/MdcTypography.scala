package io.taig.schelm.mdc

import io.taig.schelm.css.data.Style
import io.taig.schelm.data.Children
import io.taig.schelm.dsl._
import io.taig.schelm.dsl.data.DslNode

/** @see https://material.io/develop/web/docs/typography */
final case class MdcTypography[+F[_], +Event](element: DslNode.Element[F, Event, MdcTheme.Text.Font])
    extends DslNode.Component[F, Event, MdcTheme.Text.Font] {
  override val render: DslNode[F, Event, MdcTheme.Text.Font] = contextual { font =>
    val style = Style.of(
      color := font.palette.main,
      fontFamily := font.family,
      lineHeight := font.lineHeight
    )

    element.copy(style = style ++ element.style)
  }
}

object MdcTypography {
  def apply(
      value: String,
      element: DslNode.Element[Nothing, Nothing, Any]
  ): MdcTypography[Nothing, Nothing] =
    MdcTypography(element.copy(children = Children.of(text(value))))

  def h1(
      value: String,
      element: DslNode.Element[Nothing, Nothing, Any] = e.h1()
  ): MdcTypography[Nothing, Nothing] = MdcTypography(value, element)

  def h2(
      value: String,
      element: DslNode.Element[Nothing, Nothing, Any] = e.h2()
  ): MdcTypography[Nothing, Nothing] = MdcTypography(value, element)

  def h3(
      value: String,
      element: DslNode.Element[Nothing, Nothing, Any] = e.h3()
  ): MdcTypography[Nothing, Nothing] = MdcTypography(value, element)

  def h4(
      value: String,
      element: DslNode.Element[Nothing, Nothing, Any] = e.h4()
  ): MdcTypography[Nothing, Nothing] = MdcTypography(value, element)

  def h5(
      value: String,
      element: DslNode.Element[Nothing, Nothing, Any] = e.h5()
  ): MdcTypography[Nothing, Nothing] = MdcTypography(value, element)

  def h6(
      value: String,
      element: DslNode.Element[Nothing, Nothing, Any] = e.h6()
  ): MdcTypography[Nothing, Nothing] = MdcTypography(value, element)

  def p(
      value: String,
      element: DslNode.Element[Nothing, Nothing, Any] = e.p()
  ): MdcTypography[Nothing, Nothing] = MdcTypography(value, element)
}

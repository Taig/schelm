package io.taig.schelm.mdc

import io.taig.schelm.data.Children
import io.taig.schelm.dsl._
import io.taig.schelm.dsl.data.DslNode

/** @see https://material.io/develop/web/docs/typography */
final case class MdcTypography[+F[_], +Event, -Context](element: DslNode.Element[F, Event, Context])
    extends DslNode.Element.Normal[F, Event, Context] {
  override def render: DslNode[F, Event, Context] = element
}

object MdcTypography {
  def apply[F[_], Event, Context](
      element: DslNode.Element[F, Event, Context],
      theme: MdcTheme.Text.Font
  ): MdcTypography[F, Event, Context] = {
    val style = styles(
      color := theme.color,
      fontFamily := theme.family,
      fontSize := theme.size,
      fontWeight := theme.weight,
      letterSpacing := theme.letterSpacing,
      lineHeight := theme.lineHeight,
      textDecoration := theme.decoration,
      textTransform := theme.transform
    )

    MdcTypography(element.copy(style = style ++ element.style))
  }

  def auto[F[_], Event](
      value: String,
      element: DslNode.Element[F, Event, Any],
      variant: MdcTheme.Text.Variant
  ): MdcTypography[F, Event, MdcTheme] =
    contextual(theme => MdcTypography(element.copy(children = Children.of(text(value))), ???))

  def h1(
      value: String,
      element: DslNode.Element[Nothing, Nothing, Any] = e.h1()
  ): MdcTypography[Nothing, Nothing, MdcTheme] =
    auto(value, element, MdcTheme.Text.Variant.Headline1)

//  def h2(
//      value: String,
//      element: DslNode.Element[Nothing, Nothing, Any] = e.h2()
//  ): MdcTypography[Nothing, Nothing] = MdcTypography(value, element)
//
//  def h3(
//      value: String,
//      element: DslNode.Element[Nothing, Nothing, Any] = e.h3()
//  ): MdcTypography[Nothing, Nothing] = MdcTypography(value, element)
//
//  def h4(
//      value: String,
//      element: DslNode.Element[Nothing, Nothing, Any] = e.h4()
//  ): MdcTypography[Nothing, Nothing] = MdcTypography(value, element)
//
//  def h5(
//      value: String,
//      element: DslNode.Element[Nothing, Nothing, Any] = e.h5()
//  ): MdcTypography[Nothing, Nothing] = MdcTypography(value, element)
//
//  def h6(
//      value: String,
//      element: DslNode.Element[Nothing, Nothing, Any] = e.h6()
//  ): MdcTypography[Nothing, Nothing] = MdcTypography(value, element)
//
//  def p(
//      value: String,
//      element: DslNode.Element[Nothing, Nothing, Any] = e.p()
//  ): MdcTypography[Nothing, Nothing] = MdcTypography(value, element)
}

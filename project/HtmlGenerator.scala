object HtmlGenerator {
  val Elements: List[(String, String)] =
    "Button" -> "button" ::
      "Div" -> "div" ::
      "H1" -> "h1" ::
      "H2" -> "h2" ::
      "H3" -> "h3" ::
      "H4" -> "h4" ::
      "H5" -> "h5" ::
      "H6" -> "h6" ::
      "Header" -> "header" ::
      "I" -> "i" ::
      "Main" -> "main" ::
      "P" -> "p" ::
      "Section" -> "section" ::
      "Span" -> "span" ::
      "Strong" -> "strong" ::
      Nil

  val Voids: List[(String, String)] =
    "Br" -> "br" ::
      "Hr" -> "hr" ::
      Nil

  def apply(): String =
    s"""package io.taig.schelm.dsl.syntax
       |
       |import io.taig.schelm.css.data.Style
       |import io.taig.schelm.data.{Attributes, Children, Lifecycle, Listeners}
       |import io.taig.schelm.dsl.{Component, Element}
       |import io.taig.schelm.dsl.util.Tagged
       |import io.taig.schelm.dsl.util.Tagged.@@
       |
       |trait html {
       |${Elements.map((normal _).tupled).mkString("\n\n")}
       |
       |${Voids.map((void _).tupled).mkString("\n\n")}
       |}
       |
       |object html extends html""".stripMargin

  def normal(className: String, tag: String): String =
    s"""  /** `<$tag></$tag>` tag */
       |  type $className[+F[_], +Event, -Context] = Element[F, Event, Context] @@ $className.Marker
       |
       |  object $className {
       |    type Marker
       |
       |    def apply[F[_], Event, Context](
       |      attributes: Attributes = Attributes.Empty,
       |      listeners: Listeners[F] = Listeners.Empty,
       |      style: Style = Style.Empty,
       |      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop,
       |      children: Children[Component[F, Event, Context]] = Children.Empty
       |    ): $className[F, Event, Context] =
       |      Tagged[Element[F, Event, Context], Marker](
       |        component.element("$tag", attributes, listeners, style, lifecycle, children))
       |  }""".stripMargin

  def void(className: String, tag: String): String =
    s"""  /** `<$tag />` tag */
       |  type $className[+F[_], +Event, -Context] = Element[F, Event, Context] @@ $className.Marker
       |
       |  object $className {
       |    type Marker
       |
       |    def apply[F[_], Event, Context](
       |      attributes: Attributes = Attributes.Empty,
       |      listeners: Listeners[F] = Listeners.Empty,
       |      style: Style = Style.Empty,
       |      lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
       |    ): $className[F, Event, Context] =
       |      Tagged[Element[F, Event, Context], Marker](
       |        component.void("$tag", attributes, listeners, style, lifecycle))
       |  }""".stripMargin
}

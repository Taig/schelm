object HtmlGenerator {
  val Elements: List[String] =
    List(
      "a",
      "body",
      "button",
      "div",
      "h1",
      "h2",
      "h3",
      "h4",
      "h5",
      "h6",
      "header",
      "i",
      "label",
      "main",
      "p",
      "section",
      "span",
      "strong",
      "textarea"
    )

  val Voids: List[String] = List("br", "hr", "input")

  def apply(): String =
    s"""package io.taig.schelm.dsl.syntax
       |
       |import io.taig.schelm.css.data.Style
       |import io.taig.schelm.data.{Attributes, Children, Lifecycle, Listeners, Tag}
       |import io.taig.schelm.dsl.Widget
       |
       |trait html {
       |${Elements.map(normal).mkString("\n\n")}
       |
       |${Voids.map(void).mkString("\n\n")}
       |}
       |
       |object html extends html""".stripMargin

  def normal(tag: String): String =
    s"""  /** `<$tag></$tag>` tag */
       |  val ${tag.capitalize}: Tag.Name = Tag.Name("$tag")
       |
       |  def $tag[F[_], Event, Context](
       |    attributes: Attributes = Attributes.Empty,
       |    listeners: Listeners[F] = Listeners.Empty,
       |    style: Style = Style.Empty,
       |    lifecycle: Lifecycle.Element[F] = Lifecycle.Noop,
       |    children: Children[Widget[F, Event, Context]] = Children.Empty
       |  ): Widget[F, Event, Context] =
       |    component.element(${tag.capitalize}, attributes, listeners, style, lifecycle, children)""".stripMargin

  def void(tag: String): String =
    s"""  /** `<$tag></$tag>` tag */
       |  val ${tag.capitalize}: Tag.Name = Tag.Name("$tag")
       |
       |  def $tag[F[_], Event, Context](
       |    attributes: Attributes = Attributes.Empty,
       |    listeners: Listeners[F] = Listeners.Empty,
       |    style: Style = Style.Empty,
       |    lifecycle: Lifecycle.Element[F] = Lifecycle.Noop
       |  ): Widget[F, Event, Context] =
       |    component.void(${tag.capitalize}, attributes, listeners, style, lifecycle)""".stripMargin
}

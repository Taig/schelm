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
       |import io.taig.schelm.data.{Children, Tag}
       |import io.taig.schelm.dsl.Widget
       |import io.taig.schelm.dsl.data.Property
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
       |    property: Property[F] = Property.Empty,
       |    children: Children[Widget[F, Event, Context]] = Children.Empty
       |  ): Widget[F, Event, Context] =
       |    component.element(${tag.capitalize}, property, children)""".stripMargin

  def void(tag: String): String =
    s"""  /** `<$tag></$tag>` tag */
       |  val ${tag.capitalize}: Tag.Name = Tag.Name("$tag")
       |
       |  def $tag[F[_], Event, Context](property: Property[F] = Property.Empty): Widget[F, Event, Context] =
       |    component.void(${tag.capitalize}, property)""".stripMargin
}

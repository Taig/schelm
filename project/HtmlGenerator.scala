object HtmlGenerator {
  val Tags: List[String] =
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
    s"""package io.taig.schelm.dsl
       |
       |import io.taig.schelm.data.Tag
       |import io.taig.schelm.dsl.Widget
       |
       |trait html {
       |${Tags.map(normal).mkString("\n\n")}
       |
       |${Voids.map(void).mkString("\n\n")}
       |}
       |
       |object html extends html""".stripMargin

  def normal(name: String): String =
    s"""  /** `<$name></$name>` tag */
       |  val ${name.capitalize}: Tag.Name = Tag.Name("$name")
       |
       |  val $name: Widget[Nothing, Nothing, Any] = tag(${name.capitalize})"""

  def void(name: String): String =
    s"""  /** `<$name />` tag */
       |  val ${name.capitalize}: Tag.Name = Tag.Name("$name")
       |
       |  val $name: Widget[Nothing, Nothing, Any] = void(${name.capitalize})"""
}

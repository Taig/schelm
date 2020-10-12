object ElementGenerator {
  val Elements: List[(String, String)] =
    "Button" -> "button" ::
      "Div" -> "div" ::
      "Header" -> "header" ::
      "I" -> "i" ::
      "Main" -> "main" ::
      "P" -> "p" ::
      "Section" -> "section" ::
      "Span" -> "span" ::
      "Strong" -> "strong" ::
      Nil

  def definitions(): String =
    s"""package io.taig.schelm.dsl.data
       |
       |import io.taig.schelm.css.data.Style
       |import io.taig.schelm.data.{Attributes, Children, Lifecycle, Listeners}
       |import io.taig.schelm.dsl.data.DslNode
       |
       |${Elements.map((definitionNormal _).tupled).mkString("\n\n")}""".stripMargin

  def keywords(): String =
    s"""package io.taig.schelm.dsl.keyword
       |
       |import io.taig.schelm.css.data.Style
       |import io.taig.schelm.data.{Attributes, Children, Lifecycle, Listeners}
       |import io.taig.schelm.dsl.data._
       |
       |trait ElementKeyword {
       |${Elements.map((keywordNormal _).tupled).mkString("\n\n")}
       |}""".stripMargin

  def definitionNormal(className: String, tag: String): String =
    s"""/** `<$tag></$tag>` tag */
       |final case class $className[F[_], +Event, -Context](
       |    attributes: Attributes,
       |    listeners: Listeners[F],
       |    style: Style,
       |    lifecycle: Lifecycle.Element[F],
       |    children: Children[DslNode[F, Event, Context]]
       |) extends DslNode.Element.Normal[F, Event, Context]("$tag") {
       |  override def copy[G[α] >: F[α], A >: Event, B <: Context](
       |      attributes: Attributes,
       |      listeners: Listeners[G],
       |      style: Style,
       |      lifecycle: Lifecycle.Element[G],
       |      children: Children[DslNode[G, A, B]]
       |  ): $className[G, A, B] = $className(attributes, listeners, style, lifecycle, children)
       |}""".stripMargin

  def keywordNormal(className: String, tag: String): String =
    s"""  @inline
       |  final def $tag[F[_], Event, Context](
       |    attributes: Attributes = Attributes.Empty,
       |    listeners: Listeners[F] = Listeners.Empty,
       |    style: Style = Style.Empty,
       |    lifecycle: Lifecycle.Element[F] = Lifecycle.Noop,
       |    children: Children[DslNode[F, Event, Context]] = Children.Empty
       |  ): $className[F, Event, Context] = $className(attributes, listeners, style, lifecycle, children)""".stripMargin
}

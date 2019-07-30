package com.ayendo.schelm.dsl

import com.ayendo.schelm.css.{Styles, Widget}
import com.ayendo.schelm.{Attributes, Children, Component}

trait WidgetDsl[A] {
  implicit def childrenBuilderToWidget(builder: ChildrenBuilder[A]): Widget[A] =
    builder.widget

  implicit def nodeBuilderToWidget(builder: NodeBuilder[A]): Widget[A] =
    builder.widget

  implicit def tagBuilderToWidget(builder: TagBuilder[A]): Widget[A] =
    builder.widget

  implicit def textToWidget(value: String): Widget[A] = text(value)

  private val emptyProperties: Attributes[A] = Attributes.empty
  private val emptyChildren: Children[Widget[A]] = Children.empty

  def widget(name: String): Widget[A] =
    Widget[A](
      Component.Element(name, emptyProperties, emptyChildren),
      Styles.Empty
    )

  def fragment(children: Widget[A]*): Widget[A] =
    Widget(
      Component.Fragment(Children.indexed(children.toList)),
      Styles.Empty
    )

  def node(name: String): NodeBuilder[A] = new NodeBuilder[A](widget(name))

  def text(value: String): Widget[A] =
    Widget(Component.Text(value), Styles.Empty)

  val a: NodeBuilder[A] = node("a")
  val abbr: NodeBuilder[A] = node("abbr")
  val address: NodeBuilder[A] = node("address")
  val area: NodeBuilder[A] = node("area")
  val article: NodeBuilder[A] = node("article")
  val aside: NodeBuilder[A] = node("aside")
  val audio: NodeBuilder[A] = node("audio")
  val b: NodeBuilder[A] = node("b")
  val base: NodeBuilder[A] = node("base")
  val blockquote: NodeBuilder[A] = node("blockquote")
  val body: NodeBuilder[A] = node("body")
  val br: NodeBuilder[A] = node("br")
  val button: NodeBuilder[A] = node("button")
  val canvas: NodeBuilder[A] = node("canvas")
  val caption: NodeBuilder[A] = node("caption")
  val cite: NodeBuilder[A] = node("cite")
  val code: NodeBuilder[A] = node("code")
  val col: NodeBuilder[A] = node("col")
  val colgroup: NodeBuilder[A] = node("colgroup")
  val command: NodeBuilder[A] = node("command")
  val data: NodeBuilder[A] = node("data")
  val datalist: NodeBuilder[A] = node("datalist")
  val dd: NodeBuilder[A] = node("dd")
  val details: NodeBuilder[A] = node("details")
  val dfn: NodeBuilder[A] = node("dfn")
  val div: NodeBuilder[A] = node("div")
  val dl: NodeBuilder[A] = node("dl")
  val dt: NodeBuilder[A] = node("dt")
  val em: NodeBuilder[A] = node("em")
  val embed: NodeBuilder[A] = node("embed")
  val fieldset: NodeBuilder[A] = node("fieldset")
  val figcaption: NodeBuilder[A] = node("figcaption")
  val figure: NodeBuilder[A] = node("figure")
  val footer: NodeBuilder[A] = node("footer")
  val form: NodeBuilder[A] = node("form")
  val h1: NodeBuilder[A] = node("h1")
  val h2: NodeBuilder[A] = node("h2")
  val h3: NodeBuilder[A] = node("h3")
  val h4: NodeBuilder[A] = node("h4")
  val h5: NodeBuilder[A] = node("h5")
  val h6: NodeBuilder[A] = node("h6")
  val head: NodeBuilder[A] = node("head")
  val header: NodeBuilder[A] = node("header")
  val hr: NodeBuilder[A] = node("hr")
  val i: NodeBuilder[A] = node("i")
  val iframe: NodeBuilder[A] = node("iframe")
  val img: NodeBuilder[A] = node("img")
  val input: NodeBuilder[A] = node("input")
  val kbd: NodeBuilder[A] = node("kbd")
  val keygen: NodeBuilder[A] = node("keygen")
  val label: NodeBuilder[A] = node("label")
  val legend: NodeBuilder[A] = node("legend")
  val li: NodeBuilder[A] = node("li")
  val link: NodeBuilder[A] = node("link")
  val main: NodeBuilder[A] = node("main")
  val map: NodeBuilder[A] = node("map")
  val math: NodeBuilder[A] = node("math")
  val menu: NodeBuilder[A] = node("menu")
  val meta: NodeBuilder[A] = node("meta")
  val meter: NodeBuilder[A] = node("meter")
  val nav: NodeBuilder[A] = node("nav")
  val noscript: NodeBuilder[A] = node("noscript")
  val obj: NodeBuilder[A] = node("object")
  val ol: NodeBuilder[A] = node("ol")
  val optgroup: NodeBuilder[A] = node("optgroup")
  val option: NodeBuilder[A] = node("option")
  val output: NodeBuilder[A] = node("output")
  val p: NodeBuilder[A] = node("p")
  val param: NodeBuilder[A] = node("param")
  val pre: NodeBuilder[A] = node("pre")
  val progress: NodeBuilder[A] = node("progress")
  val q: NodeBuilder[A] = node("q")
  val s: NodeBuilder[A] = node("s")
  val samp: NodeBuilder[A] = node("samp")
  val script: NodeBuilder[A] = node("script")
  val section: NodeBuilder[A] = node("section")
  val select: NodeBuilder[A] = node("select")
  val small: NodeBuilder[A] = node("small")
  val source: NodeBuilder[A] = node("source")
  val span: NodeBuilder[A] = node("span")
  val strong: NodeBuilder[A] = node("strong")
  val style: NodeBuilder[A] = node("style")
  val sub: NodeBuilder[A] = node("sub")
  val summary: NodeBuilder[A] = node("summary")
  val sup: NodeBuilder[A] = node("sup")
  val svg: NodeBuilder[A] = node("svg")
  val table: NodeBuilder[A] = node("table")
  val tbody: NodeBuilder[A] = node("tbody")
  val td: NodeBuilder[A] = node("td")
  val textarea: NodeBuilder[A] = node("textarea")
  val tfoot: NodeBuilder[A] = node("tfoot")
  val th: NodeBuilder[A] = node("th")
  val thead: NodeBuilder[A] = node("thead")
  val time: NodeBuilder[A] = node("time")
  val title: NodeBuilder[A] = node("title")
  val tr: NodeBuilder[A] = node("tr")
  val track: NodeBuilder[A] = node("track")
  val u: NodeBuilder[A] = node("u")
  val ul: NodeBuilder[A] = node("ul")
  val video: NodeBuilder[A] = node("video")
  val wbr: NodeBuilder[A] = node("wbr")
}

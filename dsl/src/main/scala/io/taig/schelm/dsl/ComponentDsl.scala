package io.taig.schelm.dsl

import io.taig.schelm.Children

trait ComponentDsl[Component[+_], Property[+_]] { self =>
  implicit def nodeBuilderToComponent[A](
      builder: ElementBuilder
  ): Component[A] =
    builder.component

  implicit def childrenBuilderToComponent[A](
      builder: ChildrenBuilder[A]
  ): Component[A] = builder.component

  implicit def childrenBuilderNothingToComponent[A](
      builder: ChildrenBuilder[Nothing]
  ): Component[A] = builder.component

  implicit def stringToText[A](value: String): Component[A] =
    text(value)

  protected def element(name: String): Component[Nothing]

  def text(value: String): Component[Nothing]

  final class ElementBuilder(val component: Component[Nothing]) {
    def apply[A](
        property: Property[A],
        properties: Property[A]*
    ): ChildrenBuilder[A] =
      new ChildrenBuilder(self.properties(component, property +: properties))

    def apply[A](child: Component[A], children: Component[A]*): Component[A] =
      self.children(component, Children.indexed(child +: children))
  }

  final class ChildrenBuilder[A](val component: Component[A]) {
    def apply(child: Component[A], children: Component[A]*): Component[A] =
      self.children(component, Children.indexed(child +: children))
  }

  protected def properties[A](
      component: Component[A],
      properties: Iterable[Property[A]]
  ): Component[A]

  protected def children[A](
      component: Component[A],
      children: Children[Component[A]]
  ): Component[A]

  final def tag(name: String): ElementBuilder =
    new ElementBuilder(element(name))

  final val a: ElementBuilder = tag("a")
  final val abbr: ElementBuilder = tag("abbr")
  final val address: ElementBuilder = tag("address")
  final val area: ElementBuilder = tag("area")
  final val article: ElementBuilder = tag("article")
  final val aside: ElementBuilder = tag("aside")
  final val audio: ElementBuilder = tag("audio")
  final val b: ElementBuilder = tag("b")
  final val base: ElementBuilder = tag("base")
  final val blockquote: ElementBuilder = tag("blockquote")
  final val body: ElementBuilder = tag("body")
  final val br: ElementBuilder = tag("br")
  final val button: ElementBuilder = tag("button")
  final val canvas: ElementBuilder = tag("canvas")
  final val caption: ElementBuilder = tag("caption")
  final val cite: ElementBuilder = tag("cite")
  final val code: ElementBuilder = tag("code")
  final val col: ElementBuilder = tag("col")
  final val colgroup: ElementBuilder = tag("colgroup")
  final val command: ElementBuilder = tag("command")
  final val data: ElementBuilder = tag("data")
  final val datalist: ElementBuilder = tag("datalist")
  final val dd: ElementBuilder = tag("dd")
  final val details: ElementBuilder = tag("details")
  final val dfn: ElementBuilder = tag("dfn")
  final val div: ElementBuilder = tag("div")
  final val dl: ElementBuilder = tag("dl")
  final val dt: ElementBuilder = tag("dt")
  final val em: ElementBuilder = tag("em")
  final val embed: ElementBuilder = tag("embed")
  final val fieldset: ElementBuilder = tag("fieldset")
  final val figcaption: ElementBuilder = tag("figcaption")
  final val figure: ElementBuilder = tag("figure")
  final val footer: ElementBuilder = tag("footer")
  final val form: ElementBuilder = tag("form")
  final val h1: ElementBuilder = tag("h1")
  final val h2: ElementBuilder = tag("h2")
  final val h3: ElementBuilder = tag("h3")
  final val h4: ElementBuilder = tag("h4")
  final val h5: ElementBuilder = tag("h5")
  final val h6: ElementBuilder = tag("h6")
  final val head: ElementBuilder = tag("head")
  final val header: ElementBuilder = tag("header")
  final val hr: ElementBuilder = tag("hr")
  final val i: ElementBuilder = tag("i")
  final val iframe: ElementBuilder = tag("iframe")
  final val img: ElementBuilder = tag("img")
  final val input: ElementBuilder = tag("input")
  final val kbd: ElementBuilder = tag("kbd")
  final val keygen: ElementBuilder = tag("keygen")
  final val label: ElementBuilder = tag("label")
  final val legend: ElementBuilder = tag("legend")
  final val li: ElementBuilder = tag("li")
  final val link: ElementBuilder = tag("link")
  final val main: ElementBuilder = tag("main")
  final val map: ElementBuilder = tag("map")
  final val math: ElementBuilder = tag("math")
  final val menu: ElementBuilder = tag("menu")
  final val meta: ElementBuilder = tag("meta")
  final val meter: ElementBuilder = tag("meter")
  final val nav: ElementBuilder = tag("nav")
  final val noscript: ElementBuilder = tag("noscript")
  final val obj: ElementBuilder = tag("object")
  final val ol: ElementBuilder = tag("ol")
  final val optgroup: ElementBuilder = tag("optgroup")
  final val option: ElementBuilder = tag("option")
  final val output: ElementBuilder = tag("output")
  final val p: ElementBuilder = tag("p")
  final val param: ElementBuilder = tag("param")
  final val pre: ElementBuilder = tag("pre")
  final val progress: ElementBuilder = tag("progress")
  final val q: ElementBuilder = tag("q")
  final val s: ElementBuilder = tag("s")
  final val samp: ElementBuilder = tag("samp")
  final val script: ElementBuilder = tag("script")
  final val section: ElementBuilder = tag("section")
  final val select: ElementBuilder = tag("select")
  final val small: ElementBuilder = tag("small")
  final val source: ElementBuilder = tag("source")
  final val span: ElementBuilder = tag("span")
  final val strong: ElementBuilder = tag("strong")
  final val sub: ElementBuilder = tag("sub")
  final val summary: ElementBuilder = tag("summary")
  final val sup: ElementBuilder = tag("sup")
  final val svg: ElementBuilder = tag("svg")
  final val table: ElementBuilder = tag("table")
  final val tbody: ElementBuilder = tag("tbody")
  final val td: ElementBuilder = tag("td")
  final val textarea: ElementBuilder = tag("textarea")
  final val tfoot: ElementBuilder = tag("tfoot")
  final val th: ElementBuilder = tag("th")
  final val thead: ElementBuilder = tag("thead")
  final val time: ElementBuilder = tag("time")
  final val title: ElementBuilder = tag("title")
  final val tr: ElementBuilder = tag("tr")
  final val track: ElementBuilder = tag("track")
  final val u: ElementBuilder = tag("u")
  final val ul: ElementBuilder = tag("ul")
  final val video: ElementBuilder = tag("video")
  final val wbr: ElementBuilder = tag("wbr")
}

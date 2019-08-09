package io.taig.schelm.dsl

import io.taig.schelm.{Attribute, Attributes, Children, Listener, Listeners}

trait ComponentDsl[Component[+_]] { self =>
  def element(name: String): Component[Nothing]

  def text(value: String): Component[Nothing]

  final implicit class Builder[A](component: Component[A]) {
    def attributes(attributes: Attribute*): Component[A] =
      self.updateAttributes(component, _ ++ Attributes.from(attributes))

    def listeners(listeners: Listener[A]*): Component[A] =
      updateListeners(component, (_: Listeners[A]) ++ Listeners.from(listeners))

    def children(children: Component[A]*): Component[A] =
      self.updateChildren(component, (_: Children[Component[A]]) ++ Children.indexed(children))
  }

  protected def updateAttributes[A](
      component: Component[A],
      f: Attributes => Attributes
  ): Component[A]

  protected def updateListeners[A](
                                     component: Component[A],
                                     f: Listeners[A] => Listeners[A]
                                   ): Component[A]

  protected def updateChildren[A](
      component: Component[A],
      f: Children[Component[A]] => Children[Component[A]]
  ): Component[A]

  final val a: Component[Nothing] = element("a")
  final val abbr: Component[Nothing] = element("abbr")
  final val address: Component[Nothing] = element("address")
  final val area: Component[Nothing] = element("area")
  final val article: Component[Nothing] = element("article")
  final val aside: Component[Nothing] = element("aside")
  final val audio: Component[Nothing] = element("audio")
  final val b: Component[Nothing] = element("b")
  final val base: Component[Nothing] = element("base")
  final val blockquote: Component[Nothing] = element("blockquote")
  final val body: Component[Nothing] = element("body")
  final val br: Component[Nothing] = element("br")
  final val button: Component[Nothing] = element("button")
  final val canvas: Component[Nothing] = element("canvas")
  final val caption: Component[Nothing] = element("caption")
  final val cite: Component[Nothing] = element("cite")
  final val code: Component[Nothing] = element("code")
  final val col: Component[Nothing] = element("col")
  final val colgroup: Component[Nothing] = element("colgroup")
  final val command: Component[Nothing] = element("command")
  final val data: Component[Nothing] = element("data")
  final val datalist: Component[Nothing] = element("datalist")
  final val dd: Component[Nothing] = element("dd")
  final val details: Component[Nothing] = element("details")
  final val dfn: Component[Nothing] = element("dfn")
  final val div: Component[Nothing] = element("div")
  final val dl: Component[Nothing] = element("dl")
  final val dt: Component[Nothing] = element("dt")
  final val em: Component[Nothing] = element("em")
  final val embed: Component[Nothing] = element("embed")
  final val fieldset: Component[Nothing] = element("fieldset")
  final val figcaption: Component[Nothing] = element("figcaption")
  final val figure: Component[Nothing] = element("figure")
  final val footer: Component[Nothing] = element("footer")
  final val form: Component[Nothing] = element("form")
  final val h1: Component[Nothing] = element("h1")
  final val h2: Component[Nothing] = element("h2")
  final val h3: Component[Nothing] = element("h3")
  final val h4: Component[Nothing] = element("h4")
  final val h5: Component[Nothing] = element("h5")
  final val h6: Component[Nothing] = element("h6")
  final val head: Component[Nothing] = element("head")
  final val header: Component[Nothing] = element("header")
  final val hr: Component[Nothing] = element("hr")
  final val i: Component[Nothing] = element("i")
  final val iframe: Component[Nothing] = element("iframe")
  final val img: Component[Nothing] = element("img")
  final val input: Component[Nothing] = element("input")
  final val kbd: Component[Nothing] = element("kbd")
  final val keygen: Component[Nothing] = element("keygen")
  final val label: Component[Nothing] = element("label")
  final val legend: Component[Nothing] = element("legend")
  final val li: Component[Nothing] = element("li")
  final val link: Component[Nothing] = element("link")
  final val main: Component[Nothing] = element("main")
  final val map: Component[Nothing] = element("map")
  final val math: Component[Nothing] = element("math")
  final val menu: Component[Nothing] = element("menu")
  final val meta: Component[Nothing] = element("meta")
  final val meter: Component[Nothing] = element("meter")
  final val nav: Component[Nothing] = element("nav")
  final val noscript: Component[Nothing] = element("noscript")
  final val obj: Component[Nothing] = element("object")
  final val ol: Component[Nothing] = element("ol")
  final val optgroup: Component[Nothing] = element("optgroup")
  final val option: Component[Nothing] = element("option")
  final val output: Component[Nothing] = element("output")
  final val p: Component[Nothing] = element("p")
  final val param: Component[Nothing] = element("param")
  final val pre: Component[Nothing] = element("pre")
  final val progress: Component[Nothing] = element("progress")
  final val q: Component[Nothing] = element("q")
  final val s: Component[Nothing] = element("s")
  final val samp: Component[Nothing] = element("samp")
  final val script: Component[Nothing] = element("script")
  final val section: Component[Nothing] = element("section")
  final val select: Component[Nothing] = element("select")
  final val small: Component[Nothing] = element("small")
  final val source: Component[Nothing] = element("source")
  final val span: Component[Nothing] = element("span")
  final val strong: Component[Nothing] = element("strong")
  final val sub: Component[Nothing] = element("sub")
  final val summary: Component[Nothing] = element("summary")
  final val sup: Component[Nothing] = element("sup")
  final val svg: Component[Nothing] = element("svg")
  final val table: Component[Nothing] = element("table")
  final val tbody: Component[Nothing] = element("tbody")
  final val td: Component[Nothing] = element("td")
  final val textarea: Component[Nothing] = element("textarea")
  final val tfoot: Component[Nothing] = element("tfoot")
  final val th: Component[Nothing] = element("th")
  final val thead: Component[Nothing] = element("thead")
  final val time: Component[Nothing] = element("time")
  final val title: Component[Nothing] = element("title")
  final val tr: Component[Nothing] = element("tr")
  final val track: Component[Nothing] = element("track")
  final val u: Component[Nothing] = element("u")
  final val ul: Component[Nothing] = element("ul")
  final val video: Component[Nothing] = element("video")
  final val wbr: Component[Nothing] = element("wbr")
}

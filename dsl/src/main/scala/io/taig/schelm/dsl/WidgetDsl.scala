package io.taig.schelm.dsl

import io.taig.schelm.{
  Attribute,
  Attributes,
  Children,
  Listener,
  Listeners,
  Widget
}

trait WidgetDsl[Context, Payload] { self =>
  def element(name: String): Widget[Nothing, Context, Payload]

  def text(value: String): Widget[Nothing, Context, Payload]

  final implicit class Builder[A](widget: Widget[A, Context, Payload]) {
    def attributes(attributes: Attribute*): Widget[A, Context, Payload] =
      self.updateAttributes(widget, _ ++ Attributes.from(attributes))

    def listeners(listeners: Listener[A]*): Widget[A, Context, Payload] =
      updateListeners[A](widget, _ ++ Listeners.from(listeners))

    def children(
        children: Widget[A, Context, Payload]*
    ): Widget[A, Context, Payload] =
      self.updateChildren[A](widget, _ ++ Children.indexed(children))
  }

  def updateAttributes[A](
      widget: Widget[A, Context, Payload],
      f: Attributes => Attributes
  ): Widget[A, Context, Payload]

  def updateListeners[A](
      widget: Widget[A, Context, Payload],
      f: Listeners[A] => Listeners[A]
  ): Widget[A, Context, Payload]

  def updateChildren[A](
      widget: Widget[A, Context, Payload],
      f: Children[Widget[A, Context, Payload]] => Children[
        Widget[A, Context, Payload]
      ]
  ): Widget[A, Context, Payload]

  final val a: Widget[Nothing, Context, Payload] = element("a")
  final val abbr: Widget[Nothing, Context, Payload] = element("abbr")
  final val address: Widget[Nothing, Context, Payload] = element("address")
  final val area: Widget[Nothing, Context, Payload] = element("area")
  final val article: Widget[Nothing, Context, Payload] = element("article")
  final val aside: Widget[Nothing, Context, Payload] = element("aside")
  final val audio: Widget[Nothing, Context, Payload] = element("audio")
  final val b: Widget[Nothing, Context, Payload] = element("b")
  final val base: Widget[Nothing, Context, Payload] = element("base")
  final val blockquote: Widget[Nothing, Context, Payload] = element(
    "blockquote"
  )
  final val body: Widget[Nothing, Context, Payload] = element("body")
  final val br: Widget[Nothing, Context, Payload] = element("br")
  final val button: Widget[Nothing, Context, Payload] = element("button")
  final val canvas: Widget[Nothing, Context, Payload] = element("canvas")
  final val caption: Widget[Nothing, Context, Payload] = element("caption")
  final val cite: Widget[Nothing, Context, Payload] = element("cite")
  final val code: Widget[Nothing, Context, Payload] = element("code")
  final val col: Widget[Nothing, Context, Payload] = element("col")
  final val colgroup: Widget[Nothing, Context, Payload] = element("colgroup")
  final val command: Widget[Nothing, Context, Payload] = element("command")
  final val data: Widget[Nothing, Context, Payload] = element("data")
  final val datalist: Widget[Nothing, Context, Payload] = element("datalist")
  final val dd: Widget[Nothing, Context, Payload] = element("dd")
  final val details: Widget[Nothing, Context, Payload] = element("details")
  final val dfn: Widget[Nothing, Context, Payload] = element("dfn")
  final val div: Widget[Nothing, Context, Payload] = element("div")
  final val dl: Widget[Nothing, Context, Payload] = element("dl")
  final val dt: Widget[Nothing, Context, Payload] = element("dt")
  final val em: Widget[Nothing, Context, Payload] = element("em")
  final val embed: Widget[Nothing, Context, Payload] = element("embed")
  final val fieldset: Widget[Nothing, Context, Payload] = element("fieldset")
  final val figcaption: Widget[Nothing, Context, Payload] = element(
    "figcaption"
  )
  final val figure: Widget[Nothing, Context, Payload] = element("figure")
  final val footer: Widget[Nothing, Context, Payload] = element("footer")
  final val form: Widget[Nothing, Context, Payload] = element("form")
  final val h1: Widget[Nothing, Context, Payload] = element("h1")
  final val h2: Widget[Nothing, Context, Payload] = element("h2")
  final val h3: Widget[Nothing, Context, Payload] = element("h3")
  final val h4: Widget[Nothing, Context, Payload] = element("h4")
  final val h5: Widget[Nothing, Context, Payload] = element("h5")
  final val h6: Widget[Nothing, Context, Payload] = element("h6")
  final val head: Widget[Nothing, Context, Payload] = element("head")
  final val header: Widget[Nothing, Context, Payload] = element("header")
  final val hr: Widget[Nothing, Context, Payload] = element("hr")
  final val i: Widget[Nothing, Context, Payload] = element("i")
  final val iframe: Widget[Nothing, Context, Payload] = element("iframe")
  final val img: Widget[Nothing, Context, Payload] = element("img")
  final val input: Widget[Nothing, Context, Payload] = element("input")
  final val kbd: Widget[Nothing, Context, Payload] = element("kbd")
  final val keygen: Widget[Nothing, Context, Payload] = element("keygen")
  final val label: Widget[Nothing, Context, Payload] = element("label")
  final val legend: Widget[Nothing, Context, Payload] = element("legend")
  final val li: Widget[Nothing, Context, Payload] = element("li")
  final val link: Widget[Nothing, Context, Payload] = element("link")
  final val main: Widget[Nothing, Context, Payload] = element("main")
  final val map: Widget[Nothing, Context, Payload] = element("map")
  final val math: Widget[Nothing, Context, Payload] = element("math")
  final val menu: Widget[Nothing, Context, Payload] = element("menu")
  final val meta: Widget[Nothing, Context, Payload] = element("meta")
  final val meter: Widget[Nothing, Context, Payload] = element("meter")
  final val nav: Widget[Nothing, Context, Payload] = element("nav")
  final val noscript: Widget[Nothing, Context, Payload] = element("noscript")
  final val obj: Widget[Nothing, Context, Payload] = element("object")
  final val ol: Widget[Nothing, Context, Payload] = element("ol")
  final val optgroup: Widget[Nothing, Context, Payload] = element("optgroup")
  final val option: Widget[Nothing, Context, Payload] = element("option")
  final val output: Widget[Nothing, Context, Payload] = element("output")
  final val p: Widget[Nothing, Context, Payload] = element("p")
  final val param: Widget[Nothing, Context, Payload] = element("param")
  final val pre: Widget[Nothing, Context, Payload] = element("pre")
  final val progress: Widget[Nothing, Context, Payload] = element("progress")
  final val q: Widget[Nothing, Context, Payload] = element("q")
  final val s: Widget[Nothing, Context, Payload] = element("s")
  final val samp: Widget[Nothing, Context, Payload] = element("samp")
  final val script: Widget[Nothing, Context, Payload] = element("script")
  final val section: Widget[Nothing, Context, Payload] = element("section")
  final val select: Widget[Nothing, Context, Payload] = element("select")
  final val small: Widget[Nothing, Context, Payload] = element("small")
  final val source: Widget[Nothing, Context, Payload] = element("source")
  final val span: Widget[Nothing, Context, Payload] = element("span")
  final val strong: Widget[Nothing, Context, Payload] = element("strong")
  final val sub: Widget[Nothing, Context, Payload] = element("sub")
  final val summary: Widget[Nothing, Context, Payload] = element("summary")
  final val sup: Widget[Nothing, Context, Payload] = element("sup")
  final val svg: Widget[Nothing, Context, Payload] = element("svg")
  final val table: Widget[Nothing, Context, Payload] = element("table")
  final val tbody: Widget[Nothing, Context, Payload] = element("tbody")
  final val td: Widget[Nothing, Context, Payload] = element("td")
  final val textarea: Widget[Nothing, Context, Payload] = element("textarea")
  final val tfoot: Widget[Nothing, Context, Payload] = element("tfoot")
  final val th: Widget[Nothing, Context, Payload] = element("th")
  final val thead: Widget[Nothing, Context, Payload] = element("thead")
  final val time: Widget[Nothing, Context, Payload] = element("time")
  final val title: Widget[Nothing, Context, Payload] = element("title")
  final val tr: Widget[Nothing, Context, Payload] = element("tr")
  final val track: Widget[Nothing, Context, Payload] = element("track")
  final val u: Widget[Nothing, Context, Payload] = element("u")
  final val ul: Widget[Nothing, Context, Payload] = element("ul")
  final val video: Widget[Nothing, Context, Payload] = element("video")
  final val wbr: Widget[Nothing, Context, Payload] = element("wbr")
}

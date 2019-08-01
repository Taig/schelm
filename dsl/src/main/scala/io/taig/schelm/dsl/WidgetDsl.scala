package io.taig.schelm.dsl

import io.taig.schelm.css.{Styles, Widget}
import io.taig.schelm.{Attributes, Children, Component}
import io.taig.schelm.Children

trait WidgetDsl {
  implicit def childrenBuilderToWidget[A](builder: ChildrenBuilder[A]): Widget[A] =
    builder.widget

  implicit def nodeBuilderToWidget[A](builder: NodeBuilder[A]): Widget[A] =
    builder.widget

  implicit def nodeBuilderNothingToWidget[A](builder: NodeBuilder[Nothing]): Widget[A] =
    builder.widget

  implicit def tagBuilderToWidget[A](builder: TagBuilder[A]): Widget[A] =
    builder.widget

  implicit def textToWidget[A](value: String): Widget[A] = text(value)

  def widget[A](name: String): Widget[A] =
    Widget[A](
      Component.Element(name, Attributes.empty, Children.empty),
      Styles.Empty
    )

  def fragment[A](children: Widget[A]*): Widget[A] =
    Widget(
      Component.Fragment(Children.indexed(children.toList)),
      Styles.Empty
    )

  def node[A](name: String): NodeBuilder[A] = new NodeBuilder[A](widget(name))

  def text[A](value: String): Widget[A] =
    Widget(Component.Text(value), Styles.Empty)

  def a[A]: NodeBuilder[A] = node("a")
  def abbr[A]: NodeBuilder[A] = node("abbr")
  def address[A]: NodeBuilder[A] = node("address")
  def area[A]: NodeBuilder[A] = node("area")
  def article[A]: NodeBuilder[A] = node("article")
  def aside[A]: NodeBuilder[A] = node("aside")
  def audio[A]: NodeBuilder[A] = node("audio")
  def b[A]: NodeBuilder[A] = node("b")
  def base[A]: NodeBuilder[A] = node("base")
  def blockquote[A]: NodeBuilder[A] = node("blockquote")
  def body[A]: NodeBuilder[A] = node("body")
  def br[A]: NodeBuilder[A] = node("br")
  def button[A]: NodeBuilder[A] = node("button")
  def canvas[A]: NodeBuilder[A] = node("canvas")
  def caption[A]: NodeBuilder[A] = node("caption")
  def cite[A]: NodeBuilder[A] = node("cite")
  def code[A]: NodeBuilder[A] = node("code")
  def col[A]: NodeBuilder[A] = node("col")
  def colgroup[A]: NodeBuilder[A] = node("colgroup")
  def command[A]: NodeBuilder[A] = node("command")
  def data[A]: NodeBuilder[A] = node("data")
  def datalist[A]: NodeBuilder[A] = node("datalist")
  def dd[A]: NodeBuilder[A] = node("dd")
  def details[A]: NodeBuilder[A] = node("details")
  def dfn[A]: NodeBuilder[A] = node("dfn")
  def div[A]: NodeBuilder[A] = node("div")
  def dl[A]: NodeBuilder[A] = node("dl")
  def dt[A]: NodeBuilder[A] = node("dt")
  def em[A]: NodeBuilder[A] = node("em")
  def embed[A]: NodeBuilder[A] = node("embed")
  def fieldset[A]: NodeBuilder[A] = node("fieldset")
  def figcaption[A]: NodeBuilder[A] = node("figcaption")
  def figure[A]: NodeBuilder[A] = node("figure")
  def footer[A]: NodeBuilder[A] = node("footer")
  def form[A]: NodeBuilder[A] = node("form")
  def h1[A]: NodeBuilder[A] = node("h1")
  def h2[A]: NodeBuilder[A] = node("h2")
  def h3[A]: NodeBuilder[A] = node("h3")
  def h4[A]: NodeBuilder[A] = node("h4")
  def h5[A]: NodeBuilder[A] = node("h5")
  def h6[A]: NodeBuilder[A] = node("h6")
  def head[A]: NodeBuilder[A] = node("head")
  def header[A]: NodeBuilder[A] = node("header")
  def hr[A]: NodeBuilder[A] = node("hr")
  def i[A]: NodeBuilder[A] = node("i")
  def iframe[A]: NodeBuilder[A] = node("iframe")
  def img[A]: NodeBuilder[A] = node("img")
  def input[A]: NodeBuilder[A] = node("input")
  def kbd[A]: NodeBuilder[A] = node("kbd")
  def keygen[A]: NodeBuilder[A] = node("keygen")
  def label[A]: NodeBuilder[A] = node("label")
  def legend[A]: NodeBuilder[A] = node("legend")
  def li[A]: NodeBuilder[A] = node("li")
  def link[A]: NodeBuilder[A] = node("link")
  def main[A]: NodeBuilder[A] = node("main")
  def map[A]: NodeBuilder[A] = node("map")
  def math[A]: NodeBuilder[A] = node("math")
  def menu[A]: NodeBuilder[A] = node("menu")
  def meta[A]: NodeBuilder[A] = node("meta")
  def meter[A]: NodeBuilder[A] = node("meter")
  def nav[A]: NodeBuilder[A] = node("nav")
  def noscript[A]: NodeBuilder[A] = node("noscript")
  def obj[A]: NodeBuilder[A] = node("object")
  def ol[A]: NodeBuilder[A] = node("ol")
  def optgroup[A]: NodeBuilder[A] = node("optgroup")
  def option[A]: NodeBuilder[A] = node("option")
  def output[A]: NodeBuilder[A] = node("output")
  def p[A]: NodeBuilder[A] = node("p")
  def param[A]: NodeBuilder[A] = node("param")
  def pre[A]: NodeBuilder[A] = node("pre")
  def progress[A]: NodeBuilder[A] = node("progress")
  def q[A]: NodeBuilder[A] = node("q")
  def s[A]: NodeBuilder[A] = node("s")
  def samp[A]: NodeBuilder[A] = node("samp")
  def script[A]: NodeBuilder[A] = node("script")
  def section[A]: NodeBuilder[A] = node("section")
  def select[A]: NodeBuilder[A] = node("select")
  def small[A]: NodeBuilder[A] = node("small")
  def source[A]: NodeBuilder[A] = node("source")
  def span[A]: NodeBuilder[A] = node("span")
  def strong[A]: NodeBuilder[A] = node("strong")
  def style[A]: NodeBuilder[A] = node("style")
  def sub[A]: NodeBuilder[A] = node("sub")
  def summary[A]: NodeBuilder[A] = node("summary")
  def sup[A]: NodeBuilder[A] = node("sup")
  def svg[A]: NodeBuilder[A] = node("svg")
  def table[A]: NodeBuilder[A] = node("table")
  def tbody[A]: NodeBuilder[A] = node("tbody")
  def td[A]: NodeBuilder[A] = node("td")
  def textarea[A]: NodeBuilder[A] = node("textarea")
  def tfoot[A]: NodeBuilder[A] = node("tfoot")
  def th[A]: NodeBuilder[A] = node("th")
  def thead[A]: NodeBuilder[A] = node("thead")
  def time[A]: NodeBuilder[A] = node("time")
  def title[A]: NodeBuilder[A] = node("title")
  def tr[A]: NodeBuilder[A] = node("tr")
  def track[A]: NodeBuilder[A] = node("track")
  def u[A]: NodeBuilder[A] = node("u")
  def ul[A]: NodeBuilder[A] = node("ul")
  def video[A]: NodeBuilder[A] = node("video")
  def wbr[A]: NodeBuilder[A] = node("wbr")
}

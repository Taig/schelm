package io.taig.schelm.css.interpreter

import cats.effect.Sync
import io.taig.schelm.algebra.{Attacher, Dom}
import io.taig.schelm.css.data.{Selector, Style}

/** Attach styles to a given `<style>` tag */
object CssStyleAttacher {
  val Id = "schelm-css"

  def apply[F[_]](dom: Dom)(parent: dom.Element)(implicit F: Sync[F]): Attacher[F, Map[Selector, Style], dom.Element] =
    new Attacher[F, Map[Selector, Style], dom.Element] {
      override def attach(styles: Map[Selector, Style]): F[dom.Element] = F.delay {
        val stylesheet = CssRenderer.render(styles)
        val text = CssPrinter(stylesheet, pretty = true)
        dom.innerHtml(parent, text)
        parent
      }
    }

  /** Create a `<style>` tag in the document's `<head>` and attach the styles to it */
  def auto[F[_]](dom: Dom)(implicit F: Sync[F]): F[Attacher[F, Map[Selector, Style], dom.Element]] =
  F.delay {
    val  style = dom.createElement("style")
      dom.setAttribute(style, "id", Id)
      dom.appendChild(dom.head, style)
    CssStyleAttacher(dom)(style)
  }
}

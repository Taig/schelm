package io.taig.schelm.css.interpreter

import cats.effect.Sync
import cats.implicits._
import io.taig.schelm.algebra.Attacher
import io.taig.schelm.css.data.{Selector, Selectors, Style}
import org.scalajs.dom.{document, StyleSheet}
import org.scalajs.dom.raw.CSSStyleSheet

import scala.scalajs.js

/** Attach styles via the CSS Object Model (CSSOM) API */
final class CssomAttacher[F[_]](stylesheet: CSSStyleSheet)(implicit F: Sync[F])
    extends Attacher[F, Map[Selector, Style], StyleSheet] {
  override def attach(styles: Map[Selector, Style]): F[StyleSheet] =
    styles.toList
      .map { case (selector, style) => CssPrinter.rules(pretty = false)(style.toRules(Selectors.of(selector))) }
      .traverse(rule => F.delay(stylesheet.insertRule(rule)))
      .as(stylesheet)
}

object CssomAttacher {
  val Id: String = "schelm-css"

  def apply[F[_]: Sync](stylesheet: CSSStyleSheet): Attacher[F, Map[Selector, Style], StyleSheet] =
    new CssomAttacher[F](stylesheet)

  /** Create a `<style>` tag in the document's `<head>` and retrieve the `CSSStyleSheet` from it */
  def auto[F[_]](implicit F: Sync[F]): F[Attacher[F, Map[Selector, Style], StyleSheet]] =
    F.delay {
      val style = document.createElement("style")
      style.setAttribute("id", Id)
      document.head.appendChild(style)
      val stylesheet = style.asInstanceOf[js.Dynamic].sheet.asInstanceOf[CSSStyleSheet]
      CssomAttacher(stylesheet)
    }
}

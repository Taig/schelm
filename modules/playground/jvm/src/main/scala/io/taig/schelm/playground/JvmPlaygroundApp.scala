package io.taig.schelm.playground

import cats.effect.{ExitCode, IO, IOApp}
import io.taig.schelm.css.interpreter.CssHtmlRenderer
import io.taig.schelm.interpreter.JsoupDom

object JvmPlaygroundApp extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val dom = JsoupDom.default[IO, Event]

    CssHtmlRenderer
      .default(dom)
      .render(PlaygroundApp.renderCss(PlaygroundApp.Initial))
      .map {
        case (nodes, styles) =>
          (styles.map {
            case (selector, style) => s"${selector} -> $style"
          } ++ nodes.map(_.outerHtml())).mkString("\n")
      }
      .flatMap(markup => IO(println(markup)))
      .as(ExitCode.Success)

//    HtmlRenderer(dom).render(PlaygroundApp.render(PlaygroundApp.Initial))
//      .map(_.map(_.outerHtml()).mkString("\n"))
//      .flatMap(markup => IO(println(markup)))
//      .as(ExitCode.Success)
  }
}

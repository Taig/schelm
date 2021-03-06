//package io.taig.schelm
//
//import cats.effect.{ExitCode, IO, IOApp}
//import cats.implicits._
//
//object Playground extends IOApp {
//  override def run(args: List[String]): IO[ExitCode] =
//    ServerDom[IO, Event].flatMap { dom =>
//      val widget = App.widget(State())
//      val html = toHtml(widget)
//      val renderer = HtmlRenderer(dom)
//      renderer
//        .render(html, Path.Empty)
//        .flatMap(node => IO(println(node.head))) *>
//        IO(ExitCode.Success)
//    }
//}

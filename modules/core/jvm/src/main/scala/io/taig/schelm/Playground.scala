//package io.taig.schelm
//
//import cats.effect.{ExitCode, IO, IOApp}
//import io.taig.schelm.data.{Attributes, Children, Html, Identifier, Lifecycle, Listeners, Node, Tag}
//
//object Playground extends IOApp {
//  override def run(args: List[String]): IO[ExitCode] = {
//    val html = Html(
//      value = Node.Namespace(
//        identifier = Identifier("foobar"),
//        node = Node.Element(
//          tag = Tag(Tag.Name("p"), Attributes.Empty, Listeners.Empty),
//          variant = Node.Element.Variant.Normal(
//            children = Children.of(
//              Node.Text(value = "Hello World!",Listeners.Empty, Lifecycle.Noop)
//            )
//          ),
//          lifecycle = Lifecycle.Noop
//        )
//      )
//    )
//
//    ???
//  }
//}

//package io.taig.schelm.interpreter
//
//import cats.Applicative
//import cats.data.Kleisli
//import cats.implicits._
//import io.taig.schelm.algebra.Renderer
//import io.taig.schelm.data.Node.Element.Variant
//import io.taig.schelm.data._
//
//import scala.collection.mutable
//
//object NamespaceHtmlListenersRenderer {
//  def apply[F[_]: Applicative]: Renderer[F, NamespaceHtml[F], ListenerRegistry[F]] = {
//    def render(
//        namespace: NamespaceHtml[F],
//        path: Path,
//        builder: mutable.Builder[(Path, Listeners[F]), ListenerRegistry[F]]
//    ): Unit = namespace.unfix match {
//      case namespace: Namespace.Identified[Node[F, NamespaceHtml[F]]] =>
//        render(NamespaceHtml(namespace.namespace), path / namespace.identifier, builder)
//      case namespace: Namespace.Anonymous[Node[F, NamespaceHtml[F]]] =>
//        namespace.value match {
//          case node: Node.Element[F, NamespaceHtml[F]] =>
//            val listeners = node.tag.listeners
//
//            if (!listeners.isEmpty) builder.addOne(path -> listeners)
//
//            node.variant match {
//              case Variant.Normal(children) => children.values.foreach(render(_, path, builder))
//              case Variant.Void             => ()
//            }
//          case node: Node.Fragment[NamespaceHtml[F]] => node.children.values.foreach(render(_, path, builder))
//          case node: Node.Text[F] =>
//            val listeners = node.listeners
//            if (!listeners.isEmpty) builder.addOne(path -> listeners)
//        }
//    }
//
//    Kleisli { namespace =>
//      val builder = ListenerRegistry.newBuilder[F]
//      render(namespace, Path.Root, builder)
//      builder.result().pure[F]
//    }
//  }
//}

//package com.ayendo.schelm
//
//import cats.effect.Sync
//import cats.implicits._
//import org.jsoup.nodes.{Element => JElement, Node => JNode, TextNode => JText}
//
//final class ServerRenderer[F[_], A](implicit F: Sync[F])
//    extends Renderer[F, A, JNode] {
//  override def render(value: Html[A]): F[Node[A, JNode]] = ???
//
////  override def render(html: Fix[Component[?, A]]): F[List[JNode]] =
////    render(html, Path.Empty)
////
////  def render(html: Fix[Component[?, A]], path: Path): F[List[JNode]] =
////    html.value match {
////      case Component.Fragment(children) =>
////        children.flatTraverse { (segment, html) =>
////          render(html, path / segment)
////        }
////      case Component.Node(name, properties, children) =>
////        for {
////          element <- F.delay(new JElement(name))
////          _ <- properties.traverse_(register(element, _))
////          children <- children.flatTraverse { (segment, html) =>
////            render(html, path / segment)
////          }
////          _ <- Jsoup.appendAll(element, children)
////        } yield List(element)
////      case Component.Text(value) => F.delay(List(new JText(value)))
////    }
////
////  def register(element: JElement, property: Property[A]): F[Unit] =
////    property match {
////      case attribute: Attribute  => register(element, attribute)
////      case listener: Listener[A] => register(element, listener)
////    }
////
////  def register(element: JElement, listener: Listener[A]): F[Unit] = F.unit
////
////  def register(element: JElement, attribute: Attribute): F[Unit] =
////    attribute.value match {
////      case Value.Multiple(values, accumulator) =>
////        Jsoup.attr(element, attribute.key, values.mkString(accumulator.value))
////      case Value.One(value) =>
////        Jsoup.attr(element, attribute.key, value)
////      case Value.Flag(value) =>
////        Jsoup.attr(element, attribute.key, value)
////    }
//}
//
//object ServerRenderer {
//  def apply[F[_]: Sync, A]: Renderer[F, A, JNode] =
//    new ServerRenderer[F, A]
//}

package io.taig.schelm

import cats.effect.Sync
import cats.implicits._
import org.jsoup.nodes.{Element => JElement, Node => JNode, TextNode => JText}

final class ServerRenderer[F[_], A](implicit F: Sync[F])
    extends Renderer[F, A, Node[A, JNode]] {

  override def render(html: Html[A]): F[Node[A, JNode]] = ???
//    html.value match {
//      case Component.Fragment(children) =>
//        children
//          .traverse((_, html) => render(html))
//          .map(children => Node(Component.Fragment(children), None))
//      case Component.Element(name, attributes, children) =>
//        for {
//          element <- F.delay(new JElement(name))
//          _ <- attributes.traverse_(register(element, _))
//          children <- children.traverse((_, html) => render(html))
//          _ <- Jsoup.appendAll(element, children.values.flatMap(_.head.toList))
//        } yield {
//          val component = Component.Element(name, attributes, children)
//          Node(component, element.some)
//        }
//      case component @ Component.Text(value) =>
//        F.delay(new JText(value)).map(node => Node(component, node.some))
//    }

  def register(element: JElement, attribute: Attribute[A]): F[Unit] =
    attribute match {
      case Attribute(key, listener: Listener[A]) =>
        register(element, key, listener)
      case Attribute(key, value: Value) => register(element, key, value)
    }

  def register(element: JElement, key: String, listener: Listener[A]): F[Unit] =
    F.unit

  def register(element: JElement, key: String, value: Value): F[Unit] =
    value match {
      case Value.Multiple(values, accumulator) =>
        Jsoup.attr(element, key, values.mkString(accumulator.value))
      case Value.One(value) =>
        Jsoup.attr(element, key, value)
      case Value.Flag(value) =>
        Jsoup.attr(element, key, value)
    }
}

object ServerRenderer {
  def apply[F[_]: Sync, A]: Renderer[F, A, Node[A, JNode]] =
    new ServerRenderer[F, A]
}

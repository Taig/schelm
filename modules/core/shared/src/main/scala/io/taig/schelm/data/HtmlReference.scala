package io.taig.schelm.data

import cats.Applicative
import cats.data.Chain
import cats.data.Chain.==:

import scala.annotation.tailrec
import io.taig.schelm.algebra.Dom
import cats.implicits._

final case class HtmlReference[F[_]](reference: NodeReference[F, HtmlReference[F]]) extends AnyVal {
  def get(path: Path): Option[HtmlReference[F]] = path.values match {
    case Chain() => this.some
    case Path.Segment.Child(Key.Index(index)) ==: tail =>
      val children = reference match {
        case NodeReference.Element(Node.Element(_, Node.Element.Variant.Normal(Children.Indexed(values)), _), _) =>
          values.some
        case NodeReference.Fragment(Node.Fragment(Children.Indexed(values))) => values.some
        case _                                                               => None
      }

      children.flatMap(_.get(index.toLong)).flatMap(_.get(Path(tail)))
    case Path.Segment.Child(Key.Identifier(identifier)) ==: tail =>
      val children = reference match {
        case NodeReference.Element(Node.Element(_, Node.Element.Variant.Normal(Children.Identified(values)), _), _) =>
          values.some
        case NodeReference.Fragment(Node.Fragment(Children.Identified(values))) => values.some
        case _                                                                  => None
      }

      children.flatMap(_.get(identifier)).flatMap(_.get(Path(tail)))
    case Path.Segment.Stateful ==: tail =>
      reference match {
        case NodeReference.Stateful(_, value) => value.get(Path(tail))
        case _                                => None
      }
  }

  def update(path: Path)(
      f: NodeReference[F, HtmlReference[F]] => F[NodeReference[F, HtmlReference[F]]]
  )(implicit F: Applicative[F]): F[HtmlReference[F]] =
    path.values match {
      case Chain() => f(this.reference).map(HtmlReference.apply)
    }

  @tailrec
  def node: Node[F, ListenerReferences[F], HtmlReference[F]] = reference match {
    case NodeReference.Element(node, _)       => node
    case NodeReference.Fragment(node)         => node
    case NodeReference.Stateful(_, reference) => reference.node
    case NodeReference.Text(node, _)          => node
  }

  def html: Html[F] = Html(node.bimap(_.toListeners, _.html))

  def dom: Vector[Dom.Node] = reference match {
    case NodeReference.Element(_, dom)        => Vector(dom)
    case NodeReference.Fragment(node)         => node.children.indexed.flatMap(_.dom)
    case NodeReference.Stateful(_, reference) => reference.dom
    case NodeReference.Text(_, dom)           => Vector(dom)
  }
}

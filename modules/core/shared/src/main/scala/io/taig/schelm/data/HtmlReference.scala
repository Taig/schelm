package io.taig.schelm.data

import cats.Applicative
import cats.data.Chain
import cats.data.Chain.==:
import cats.implicits._
import io.taig.schelm.algebra.Dom
import io.taig.schelm.util.PathTraversal

final case class HtmlReference[F[_]](reference: NodeReference[F, HtmlReference[F]]) extends AnyVal {
  def get(path: Path): Option[HtmlReference[F]] = path.values match {
    case Chain() => this.some
    case Key.Index(index) ==: tail =>
      val children = reference match {
        case NodeReference.Element(Node.Element(_, Node.Element.Variant.Normal(Children.Indexed(values)), _), _) =>
          values.some
        case NodeReference.Fragment(Node.Fragment(Children.Indexed(values))) => values.some
        case _                                                               => None
      }

      children.flatMap(_.get(index.toLong)).flatMap(_.get(Path(tail)))
    case Key.Identifier(identifier) ==: tail =>
      val children = reference match {
        case NodeReference.Element(Node.Element(_, Node.Element.Variant.Normal(Children.Identified(values)), _), _) =>
          values.some
        case NodeReference.Fragment(Node.Fragment(Children.Identified(values))) => values.some
        case _                                                                  => None
      }

      children.flatMap(_.get(identifier)).flatMap(_.get(Path(tail)))
  }

  def update(path: Path)(
      f: NodeReference[F, HtmlReference[F]] => F[NodeReference[F, HtmlReference[F]]]
  )(implicit F: Applicative[F]): F[HtmlReference[F]] =
    path.values match {
      case Chain() => f(this.reference).map(HtmlReference.apply)
    }

  def node: Node[F, ListenerReferences[F], HtmlReference[F]] = reference match {
    case NodeReference.Element(node, _) => node
    case NodeReference.Fragment(node)   => node
    case NodeReference.Text(node, _)    => node
  }

  def html: Html[F] = Html(node.bimap(_.toListeners, _.html))

  def dom: Vector[Dom.Node] = reference match {
    case NodeReference.Element(_, dom) => Vector(dom)
    case NodeReference.Fragment(node)  => node.children.indexed.flatMap(_.dom)
    case NodeReference.Text(_, dom)    => Vector(dom)
  }
}

object HtmlReference {
  implicit def traversal[F[_]]: PathTraversal[HtmlReference[F]] =
    PathTraversal.ofReference[F, HtmlReference[F]](_.reference, (html, reference) => html.copy(reference = reference))
}

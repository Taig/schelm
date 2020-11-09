package io.taig.schelm.util

import scala.annotation.tailrec

import cats.Applicative
import io.taig.schelm.data.Listener.Action
import io.taig.schelm.data.Node.Element.Variant
import io.taig.schelm.data.Path./
import io.taig.schelm.data._

trait PathTraversal[F[_[_]], G[_]] {
  def find(value: F[G])(path: Path): Option[F[G]]

  def listener(value: F[G])(name: Listener.Name): Option[Listener.Action[G]]

  def modify[H[_]: Applicative](value: F[G])(path: Path)(f: F[G] => H[F[G]]): H[F[G]]
}

object PathTraversal {
  @inline
  def apply[F[_[_]], G[_]](implicit traversal: PathTraversal[F, G]): PathTraversal[F, G] = traversal

  implicit def html[F[_]]: PathTraversal[Html, F] = ???

  def ofNode[F[_[_]], G[_], Listeners](
      extract: F[G] => Node[G, Listeners, F[G]],
      lift: (F[G], Node[G, Listeners, F[G]]) => F[G],
      get: (Listeners, Listener.Name) => Option[Listener.Action[G]]
  ): PathTraversal[F, G] =
    new PathTraversal[F, G] {
      def children(node: Node[G, Listeners, F[G]]): Children[F[G]] = node match {
        case node: Node.Element[G, Listeners, F[G]] =>
          node.variant match {
            case Variant.Normal(children) => children
            case Variant.Void             => Children.Empty
          }
        case Node.Fragment(children) => children
        case _: Node.Text[_, _]      => Children.Empty
      }

      @tailrec
      override def find(value: F[G])(path: Path): Option[F[G]] = path match {
        case Path.Root => Some(value)
        case head / tail =>
          children(extract(value)).get(head) match {
            case Some(child) => find(child)(tail)
            case None        => None
          }
      }

      override def listener(value: F[G])(name: Listener.Name): Option[Action[G]] = extract(value) match {
        case node: Node.Element[_, Listeners, F[G]] => get(node.tag.listeners, name)
        case _: Node.Fragment[_]                    => None
        case node: Node.Text[_, Listeners]          => get(node.listeners, name)
      }

      override def modify[H[_]: Applicative](value: F[G])(path: Path)(f: F[G] => H[F[G]]): H[F[G]] = ???
//      override def modify[G[_]: Applicative](value: A)(path: Path)(f: A => G[A]): G[A] =
//        path.values match {
//          case Chain.nil => f(value)
//          case Key.Index(index) ==: tail =>
//            extract(value) match {
//              case node: Node.Element[F, Listeners, A] =>
//                node.variant match {
//                  case Variant.Normal(children @ Children.Indexed(values)) =>
//                    values.get(index.toLong) match {
//                      case Some(child) =>
//                        modify(child)(Path(tail))(f).map { update =>
//                          lift(value, node.copy(variant = Node.Element.Variant.Normal(children.updated(index, update))))
//                        }
//                      case None => value.pure[G]
//                    }
//                  case Variant.Normal(Children.Identified(_)) | Variant.Void => value.pure[G]
//                }
//              case node @ Node.Fragment(children @ Children.Indexed(values)) =>
//                values.get(index.toLong) match {
//                  case Some(child) =>
//                    modify(child)(Path(tail))(f).map { update =>
//                      lift(value, node.copy(children = children.updated(index, update)))
//                    }
//                  case None => value.pure[G]
//                }
//              case _ => value.pure[G]
//            }
//        }
    }

//  def ofReference[F[_], Listeners, A](
//      extract: A => NodeReference[F, Listeners, A],
//      lift: (A, NodeReference[F, Listeners, A]) => A
//  ): PathTraversal[A] =
//    new PathTraversal[A] {
//      override def find(reference: A)(path: Path): Option[A] = path.values match {
//        case Chain.nil => reference.some
//        case Key.Index(index) ==: tail =>
//          val children: Option[Vector[A]] = extract(reference) match {
//            case NodeReference.Element(Node.Element(_, Node.Element.Variant.Normal(Children.Indexed(values)), _), _) =>
//              values.some
//            case NodeReference.Fragment(Node.Fragment(Children.Indexed(values))) => values.some
//            case _                                                               => None
//          }
//
//          children.flatMap(_.get(index.toLong)).flatMap(child => find(child)(Path(tail)))
//        case Key.Identifier(identifier) ==: tail =>
//          val children: Option[VectorMap[String, A]] = extract(reference) match {
//            case NodeReference
//                  .Element(Node.Element(_, Node.Element.Variant.Normal(Children.Identified(values)), _), _) =>
//              values.some
//            case NodeReference.Fragment(Node.Fragment(Children.Identified(values))) => values.some
//            case _                                                                  => None
//          }
//
//          children.flatMap(_.get(identifier)).flatMap(child => find(child)(Path(tail)))
//      }
//
//      override def modify[G[_]: Applicative](value: A)(path: Path)(f: A => G[A]): G[A] =
//        path.values match {
//          case Chain.nil => f(value)
//          case Key.Index(index) ==: tail =>
//            extract(value) match {
//              case reference: NodeReference.Element[F, Listeners, A] =>
//                reference.node.variant match {
//                  case Variant.Normal(children @ Children.Indexed(values)) =>
//                    values.get(index.toLong) match {
//                      case Some(child) =>
//                        modify(child)(Path(tail))(f).map { update =>
//                          lift(
//                            value,
//                            reference.copy(node =
//                              reference.node
//                                .copy(variant = Node.Element.Variant.Normal(children.updated(index, update)))
//                            )
//                          )
//                        }
//                      case None => value.pure[G]
//                    }
//                  case Variant.Normal(Children.Identified(_)) | Variant.Void => value.pure[G]
//                }
//              case reference @ NodeReference.Fragment(Node.Fragment(children @ Children.Indexed(values))) =>
//                values.get(index.toLong) match {
//                  case Some(child) =>
//                    modify(child)(Path(tail))(f).map { update =>
//                      lift(
//                        value,
//                        reference.copy(node = reference.node.copy(children = children.updated(index, update)))
//                      )
//                    }
//                  case None => value.pure[G]
//                }
//              case _ => value.pure[G]
//            }
//        }
//    }
}

package io.taig.schelm.util

import cats.Applicative
import cats.data.Chain
import cats.data.Chain.==:
import cats.implicits._
import io.taig.schelm.data.Node.Element.Variant
import io.taig.schelm.data._
import simulacrum.typeclass

import scala.collection.immutable.VectorMap

@typeclass
trait PathTraversal[A] {
  def get(value: A)(path: Path): Option[A]

  def modify[F[_]: Applicative](value: A)(path: Path)(f: A => F[A]): F[A]
}

object PathTraversal {
  def ofReference[F[_], A](extract: A => NodeReference[F, A], lift: (A, NodeReference[F, A]) => A): PathTraversal[A] =
    new PathTraversal[A] {
      override def get(reference: A)(path: Path): Option[A] = path.values match {
        case Chain() => reference.some
        case Key.Index(index) ==: tail =>
          val children: Option[Vector[A]] = extract(reference) match {
            case NodeReference.Element(Node.Element(_, Node.Element.Variant.Normal(Children.Indexed(values)), _), _) =>
              values.some
            case NodeReference.Fragment(Node.Fragment(Children.Indexed(values))) => values.some
            case _                                                               => None
          }

          children.flatMap(_.get(index.toLong)).flatMap(child => get(child)(Path(tail)))
        case Key.Identifier(identifier) ==: tail =>
          val children: Option[VectorMap[String, A]] = extract(reference) match {
            case NodeReference
                  .Element(Node.Element(_, Node.Element.Variant.Normal(Children.Identified(values)), _), _) =>
              values.some
            case NodeReference.Fragment(Node.Fragment(Children.Identified(values))) => values.some
            case _                                                                  => None
          }

          children.flatMap(_.get(identifier)).flatMap(child => get(child)(Path(tail)))
      }

      override def modify[G[_]: Applicative](value: A)(path: Path)(f: A => G[A]): G[A] =
        path.values match {
          case Chain() => f(value)
          case Key.Index(index) ==: tail =>
            extract(value) match {
              case reference: NodeReference.Element[F, A] =>
                reference.node.variant match {
                  case Variant.Normal(children @ Children.Indexed(values)) =>
                    values.get(index.toLong) match {
                      case Some(child) =>
                        modify(child)(Path(tail))(f).map { update =>
                          lift(
                            value,
                            reference.copy(node =
                              reference.node
                                .copy(variant = Node.Element.Variant.Normal(children.updated(index, update)))
                            )
                          )
                        }
                      case None => value.pure[G]
                    }
                  case Variant.Normal(Children.Identified(_)) | Variant.Void => value.pure[G]
                }
              case reference @ NodeReference.Fragment(Node.Fragment(children @ Children.Indexed(values))) =>
                values.get(index.toLong) match {
                  case Some(child) =>
                    modify(child)(Path(tail))(f).map { update =>
                      lift(
                        value,
                        reference.copy(node = reference.node.copy(children = children.updated(index, update)))
                      )
                    }
                  case None => value.pure[G]
                }
              case _ => value.pure[G]
            }
        }
    }
}

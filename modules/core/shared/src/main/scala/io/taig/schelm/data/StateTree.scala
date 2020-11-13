package io.taig.schelm.data

import cats._
import cats.implicits._
import io.taig.schelm.data.Identification./

import scala.annotation.tailrec

final case class StateTree[+A](states: Map[Identifier, A], children: Map[Identifier, StateTree[A]]) {
  @tailrec
  def find(identification: Identification): Option[StateTree[A]] = identification match {
    case Identification.Empty => Some(this)
    case head / tail =>
      children.get(head) match {
        case Some(child) => child.find(tail)
        case None        => None
      }
  }

  @inline
  def get(identifier: Identifier): Option[StateTree[A]] = children.get(identifier)

//  def updatedWith[B >: A](path: Path)(f: Option[StateTree[B]] => Option[StateTree[B]]): StateTree[B] = path match {
//    case head / Path.Root => copy(children = children.updatedWith[StateTree[B]](head)(f))
//    case head / tail =>
//      children.get(head) match {
//        case Some(child: StateTree[B]) => copy(children = children.updated(head, child.updatedWith[B](tail)(f)))
//        case None                     => this
//      }
//    case Path.Root => f(Some(this)).getOrElse(copy(children = Map.empty))
//  }
//
//  def delete(path: Path): StateTree[A] = updatedWith(path)(_ => None)
//
  def merge[B >: A](tree: StateTree[B]): StateTree[B] = StateTree(states ++ tree.states, children ++ tree.children)
}

object StateTree {
  def empty[A]: StateTree[A] = StateTree(states = Map.empty, children = Map.empty)

  val Empty: StateTree[Nothing] = empty

  implicit def monoid[A]: Monoid[StateTree[A]] = new Monoid[StateTree[A]] {
    override val empty: StateTree[A] = Empty

    override def combine(x: StateTree[A], y: StateTree[A]): StateTree[A] = x merge y
  }

  implicit def eq[A: Eq]: Eq[StateTree[A]] = new Eq[StateTree[A]] {
    override def eqv(x: StateTree[A], y: StateTree[A]): Boolean =
      x.states === y.states && x.children === y.children
  }
}

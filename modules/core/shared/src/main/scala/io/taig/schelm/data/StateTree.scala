package io.taig.schelm.data

import cats._
import cats.implicits._
import io.taig.schelm.data.Path./

import scala.annotation.tailrec

final case class StateTree[+A](values: Vector[A], children: Map[Key, StateTree[A]]) { self =>
  @tailrec
  def find(path: Path): Option[StateTree[A]] = path match {
    case Path.Root => Some(this)
    case head / tail =>
      children.get(head) match {
        case Some(child) => child.find(tail)
        case None        => None
      }
  }

  def get(key: Key): Option[StateTree[A]] = children.get(key)

  def updatedWith[B >: A](path: Path)(f: Option[StateTree[B]] => Option[StateTree[B]]): StateTree[B] = path match {
    case head / Path.Root => copy(children = children.updatedWith[StateTree[B]](head)(f))
    case head / tail =>
      children.get(head) match {
        case Some(child: StateTree[B]) => copy(children = children.updated(head, child.updatedWith[B](tail)(f)))
        case None                      => this
      }
    case Path.Root => f(Some(this)).getOrElse(StateTree.Empty)
  }

  def delete(path: Path): StateTree[A] = updatedWith(path)(_ => None)

  def find(path: Path, state: Int): Option[A] = find(path).flatMap(_.values.lift(state))

  def updatedState[B >: A](path: Path, value: B, index: Int): StateTree[B] =
    updatedWith[B](path) {
      case Some(tree) =>
        try Some(tree.copy(values = tree.values.updated(index, value)))
        catch { case _: IndexOutOfBoundsException => Some(tree) }
      case None => None
    }

  def merge[B >: A](tree: StateTree[B]): StateTree[B] = {
    val selfValuesLength = self.values.length
    val selfChildrenKeys = self.children.keySet
    val treeValuesLength = tree.values.length
    val treeChildrenKeys = tree.children.keySet

    val keysSelfOnly = selfChildrenKeys diff treeChildrenKeys
    val keysTreeOnly = treeChildrenKeys diff selfChildrenKeys
    val keysBoth = selfChildrenKeys intersect treeChildrenKeys

    val values =
      if (treeValuesLength >= selfValuesLength) tree.values
      else tree.values ++ self.values.slice(treeValuesLength, selfValuesLength)

    val children = keysSelfOnly.map(key => key -> self.children(key)) ++
      keysTreeOnly.map(key => key -> tree.children(key)) ++
      keysBoth.map(key => key -> self.children(key).merge(tree.children(key)))

    StateTree(values, children.toMap)
  }
}

object StateTree {
  val Empty: StateTree[Nothing] = StateTree(values = Vector.empty, children = Map.empty)

  implicit val monoidK: MonoidK[StateTree] = new MonoidK[StateTree] {
    override def empty[A]: StateTree[A] = Empty

    override def combineK[A](x: StateTree[A], y: StateTree[A]): StateTree[A] = x merge y
  }

  implicit def monoid[A]: Monoid[StateTree[A]] = monoidK.algebra[A]

  implicit def eq[A: Eq]: Eq[StateTree[A]] = new Eq[StateTree[A]] {
    override def eqv(x: StateTree[A], y: StateTree[A]): Boolean =
      x.values === y.values && x.children === y.children
  }
}

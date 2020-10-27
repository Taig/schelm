package io.taig.schelm.data

import io.taig.schelm.data.Path./

import scala.annotation.tailrec

final case class StateTree[+A](values: Vector[A], children: Map[Key, StateTree[A]]) {
  @tailrec
  def get(path: Path): Option[StateTree[A]] = path match {
    case Path.Root => Some(this)
    case head / tail =>
      children.get(head) match {
        case Some(child) => child.get(tail)
        case None        => None
      }
  }

  def select(key: Key): Option[StateTree[A]] = children.get(key)

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

  def find(path: Path, state: Int): Option[A] = get(path).flatMap(_.values.lift(state))

  def updatedState[B >: A](path: Path, state: Int, value: B): StateTree[B] =
    updatedWith[B](path) {
      case Some(tree) =>
        try Some(tree.copy(values = tree.values.updated(state, value)))
        catch { case _: IndexOutOfBoundsException => Some(tree) }
      case None => None
    }
}

object StateTree {
  val Empty: StateTree[Nothing] = StateTree(values = Vector.empty, children = Map.empty)
}

package io.taig.schelm.data

import io.taig.schelm.instance.ChildrenInstances
import io.taig.schelm.util.Text

final case class Children[+A](values: Vector[A]) extends AnyVal {
  def isEmpty: Boolean = values.isEmpty

  def get(index: Int): Option[A] = values.lift(index)

  def ++[B >: A](children: Children[B]): Children[B] = Children(values ++ children.values)

  def map[B](f: A => B): Children[B] = Children(values.map(f))

  def flatMap[B](f: A => Children[B]): Children[B] = Children(values.flatMap(f(_).values))

  def toList: List[A] = values.toList

  override def toString: String = if (isEmpty) "[]"
  else
    s"""[
       |  ${Text.align(2)(values.mkString(",\n"))}
       |]""".stripMargin
}

object Children extends ChildrenInstances {
  val Empty: Children[Nothing] = Children(Vector.empty)

  def from[A](children: Iterable[A]): Children[A] = Children(children.toVector)

  def of[A](children: A*): Children[A] = from(children)

  def one[A](child: A): Children[A] = of(child)
}

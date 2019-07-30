package com.ayendo.schelm

import cats.Eq
import cats.data.Ior
import cats.implicits._

object Differ {
  def diff[A: Eq](previous: Html[A], next: Html[A]): Option[Diff[A]] =
    (previous.value, next.value) match {
      case (Component.Text(previous), Component.Text(next)) =>
        text(previous, next)
      case (Component.Fragment(previous), Component.Fragment(next)) =>
        children(previous, next)
      case (
          previous: Component.Element[Html[A], A],
          next: Component.Element[Html[A], A]
          ) =>
        node(previous, next)
      case (_, next) => Diff.Replace(Html(next)).some
    }

  def node[A: Eq](
      previous: Component.Element[Html[A], A],
      next: Component.Element[Html[A], A]
  ): Option[Diff[A]] =
    if (previous.name =!= next.name) Diff.Replace(Html(next)).some
    else {
      val diffs = List(
        attributes[A](previous.attributes, next.attributes),
        children[A](previous.children, next.children)
      ).flattenOption

      Diff.from(diffs)
    }

  def attributes[A: Eq](
      previous: Attributes[A],
      next: Attributes[A]
  ): Option[Diff[A]] =
    if (previous.isEmpty && next.isEmpty) None
    else if (previous.isEmpty) Diff.from(next.map(Diff.AddAttribute.apply))
    else if (next.isEmpty)
      Diff.from(previous.map(attribute => Diff.RemoveAttribute(attribute.key)))
    else compareAttributes(previous, next)

  def compareAttributes[A: Eq](
      previous: Attributes[A],
      next: Attributes[A]
  ): Option[Diff[A]] = {
    val diffs = (previous zipAll next).mapFilter {
      case (key, Ior.Both(previous, next)) =>
        if (previous === next) None
        else Diff.UpdateAttribute(Attribute(key, next)).some
      case (key, Ior.Left(_)) => Diff.RemoveAttribute(key).some
      case (key, Ior.Right(next)) =>
        Diff.AddAttribute(Attribute(key, next)).some
    }

    Diff.from(diffs)
  }

  def children[A: Eq](
      previous: Children[Html[A]],
      next: Children[Html[A]]
  ): Option[Diff[A]] =
    if (previous.isEmpty && next.isEmpty) None
    else if (previous.isEmpty) Diff.from(next.map(Diff.AddChild.apply))
    else if (next.isEmpty)
      Diff.from(previous.map((key, _) => Diff.RemoveChild(key)))
    else compareChildren(previous, next)

  def compareChildren[A: Eq](
      previous: Children[Html[A]],
      next: Children[Html[A]]
  ): Option[Diff[A]] = {
    val diffs = (previous zipAll next).mapFilter {
      case (key, Ior.Both(previous, next)) =>
        diff(previous, next).map(Diff.UpdateChild[A](key, _))
      case (key, Ior.Left(_))     => Diff.RemoveChild(key).some
      case (key, Ior.Right(next)) => Diff.AddChild(key, next).some
    }

    Diff.from(diffs)
  }

  def text[A](previous: String, next: String): Option[Diff[A]] =
    if (previous =!= next) Diff.UpdateText(next).some else None
}

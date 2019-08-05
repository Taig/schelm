package io.taig.schelm

import cats.data.Ior
import cats.implicits._

final class HtmlDiffer[A] extends Differ[Html[A], Diff[A]] {
  override def diff(previous: Html[A], next: Html[A]): Option[Diff[A]] =
    (previous.value, next.value) match {
      case (previous: Component.Text, next: Component.Text) =>
        text(previous, next)
      case (
          previous: Component.Fragment[Html[A]],
          next: Component.Fragment[Html[A]]
          ) =>
        fragment(previous, next)
      case (
          previous: Component.Element[Html[A], A],
          next: Component.Element[Html[A], A]
          ) =>
        element(previous, next)
      case (
          previous: Component.Lazy[Html[A], A],
          next: Component.Lazy[Html[A], A]
          ) =>
        lzy(previous, next)
      case (_, next) => Diff.Replace(Html(next)).some
    }

  def element(
      previous: Component.Element[Html[A], A],
      next: Component.Element[Html[A], A]
  ): Option[Diff[A]] =
    if (previous.name =!= next.name) Diff.Replace(Html(next)).some
    else {
      val diffs = List(
        attributes(previous.attributes, next.attributes),
        children(previous.children, next.children)
      ).flattenOption

      Diff.from(diffs)
    }

  def attributes(
      previous: Attributes[A],
      next: Attributes[A]
  ): Option[Diff[A]] =
    if (previous.isEmpty && next.isEmpty) None
    else if (previous.isEmpty) Diff.from(next.map(Diff.AddAttribute.apply))
    else if (next.isEmpty)
      Diff.from(previous.map(attribute => Diff.RemoveAttribute(attribute.key)))
    else compareAttributes(previous, next)

  def compareAttributes(
      previous: Attributes[A],
      next: Attributes[A]
  ): Option[Diff[A]] = {
    val diffs = (previous zipAll next).mapFilter {
      case (key, Ior.Both(previous, next)) =>
        if (previous == next) None
        else Diff.UpdateAttribute(Attribute(key, next)).some
      case (key, Ior.Left(_)) => Diff.RemoveAttribute(key).some
      case (key, Ior.Right(next)) =>
        Diff.AddAttribute(Attribute(key, next)).some
    }

    Diff.from(diffs)
  }

  def children(
      previous: Children[Html[A]],
      next: Children[Html[A]]
  ): Option[Diff[A]] =
    if (previous.isEmpty && next.isEmpty) None
    else if (previous.isEmpty)
      Diff.from(next.toList.map(Diff.AddChild.apply[A] _ tupled))
    else if (next.isEmpty)
      Diff.from(previous.keys.map(Diff.RemoveChild))
    else compareChildren(previous, next)

  def compareChildren(
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

  def fragment(
      previous: Component.Fragment[Html[A]],
      next: Component.Fragment[Html[A]]
  ): Option[Diff[A]] =
    children(previous.children, next.children)

  def lzy(
      previous: Component.Lazy[Html[A], A],
      next: Component.Lazy[Html[A], A]
  ): Option[Diff[A]] =
    if (previous.hash == next.hash) None
    else diff(previous.component.value, next.component.value)

  def text(previous: Component.Text, next: Component.Text): Option[Diff[A]] =
    if (previous.value != next.value) Diff.UpdateText(next.value).some else None
}

object HtmlDiffer {
  def apply[A]: Differ[Html[A], Diff[A]] = new HtmlDiffer[A]
}

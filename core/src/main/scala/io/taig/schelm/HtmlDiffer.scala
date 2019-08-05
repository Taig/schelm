package io.taig.schelm

import cats.data.Ior
import cats.implicits._

final class HtmlDiffer[A] extends Differ[Html[A], HtmlDiff[A]] {
  override def diff(previous: Html[A], next: Html[A]): Option[HtmlDiff[A]] =
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
      case (previous: Component.Lazy[Html[A]], next: Component.Lazy[Html[A]]) =>
        lzy(previous, next)
      case (_, next) => HtmlDiff.Replace(Html(next)).some
    }

  def element(
      previous: Component.Element[Html[A], A],
      next: Component.Element[Html[A], A]
  ): Option[HtmlDiff[A]] =
    if (previous.name =!= next.name) HtmlDiff.Replace(Html(next)).some
    else {
      val diffs = List(
        attributes(previous.attributes, next.attributes),
        children(previous.children, next.children)
      ).flattenOption

      HtmlDiff.from(diffs)
    }

  def attributes(
      previous: Attributes[A],
      next: Attributes[A]
  ): Option[HtmlDiff[A]] =
    if (previous.isEmpty && next.isEmpty) None
    else if (previous.isEmpty)
      HtmlDiff.from(next.map(HtmlDiff.AddAttribute.apply))
    else if (next.isEmpty)
      HtmlDiff.from(
        previous.map(attribute => HtmlDiff.RemoveAttribute(attribute.key))
      )
    else compareAttributes(previous, next)

  def compareAttributes(
      previous: Attributes[A],
      next: Attributes[A]
  ): Option[HtmlDiff[A]] = {
    val diffs = (previous zipAll next).mapFilter {
      case (key, Ior.Both(previous, next)) =>
        if (previous == next) None
        else HtmlDiff.UpdateAttribute(Attribute(key, next)).some
      case (key, Ior.Left(_)) => HtmlDiff.RemoveAttribute(key).some
      case (key, Ior.Right(next)) =>
        HtmlDiff.AddAttribute(Attribute(key, next)).some
    }

    HtmlDiff.from(diffs)
  }

  def children(
      previous: Children[Html[A]],
      next: Children[Html[A]]
  ): Option[HtmlDiff[A]] =
    if (previous.isEmpty && next.isEmpty) None
    else if (previous.isEmpty)
      HtmlDiff.from(next.toList.map(HtmlDiff.AddChild.apply[A] _ tupled))
    else if (next.isEmpty)
      HtmlDiff.from(previous.keys.map(HtmlDiff.RemoveChild))
    else compareChildren(previous, next)

  def compareChildren(
      previous: Children[Html[A]],
      next: Children[Html[A]]
  ): Option[HtmlDiff[A]] = {
    val diffs = (previous zipAll next).mapFilter {
      case (key, Ior.Both(previous, next)) =>
        diff(previous, next).map(HtmlDiff.UpdateChild[A](key, _))
      case (key, Ior.Left(_))     => HtmlDiff.RemoveChild(key).some
      case (key, Ior.Right(next)) => HtmlDiff.AddChild(key, next).some
    }

    HtmlDiff.from(diffs)
  }

  def fragment(
      previous: Component.Fragment[Html[A]],
      next: Component.Fragment[Html[A]]
  ): Option[HtmlDiff[A]] =
    children(previous.children, next.children)

  def lzy(
      previous: Component.Lazy[Html[A]],
      next: Component.Lazy[Html[A]]
  ): Option[HtmlDiff[A]] =
    if (previous.hash == next.hash) None
    else diff(previous.component.value, next.component.value)

  def text(
      previous: Component.Text,
      next: Component.Text
  ): Option[HtmlDiff[A]] =
    if (previous.value != next.value) HtmlDiff.UpdateText(next.value).some
    else None
}

object HtmlDiffer {
  def apply[A]: Differ[Html[A], HtmlDiff[A]] = new HtmlDiffer[A]
}

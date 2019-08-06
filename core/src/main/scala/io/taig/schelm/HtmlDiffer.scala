package io.taig.schelm

import cats.data.Ior
import cats.implicits._

final class HtmlDiffer[Event] extends Differ[Html[Event], HtmlDiff[Event]] {
  override def diff(
      previous: Html[Event],
      next: Html[Event]
  ): Option[HtmlDiff[Event]] =
    (previous.value, next.value) match {
      case (previous: Component.Text, next: Component.Text) =>
        text(previous, next)
      case (
          previous: Component.Fragment[Html[Event]],
          next: Component.Fragment[Html[Event]]
          ) =>
        children(previous.children, next.children)
      case (
          previous: Component.Element[Html[Event], Event],
          next: Component.Element[Html[Event], Event]
          ) =>
        element(previous, next)
      case (_, next) => HtmlDiff.Replace(Html(next)).some
    }

  def element(
      previous: Component.Element[Html[Event], Event],
      next: Component.Element[Html[Event], Event]
  ): Option[HtmlDiff[Event]] =
    if (previous.name =!= next.name) HtmlDiff.Replace(Html(next)).some
    else {
      val diffs = List(
        attributes(previous.attributes, next.attributes),
        children(previous.children, next.children)
      ).flattenOption

      HtmlDiff.from(diffs)
    }

  def text(
      previous: Component.Text,
      next: Component.Text
  ): Option[HtmlDiff[Event]] =
    if (previous.value != next.value) HtmlDiff.UpdateText(next.value).some
    else None

  def attributes(
      previous: Attributes[Event],
      next: Attributes[Event]
  ): Option[HtmlDiff[Event]] =
    if (previous.isEmpty && next.isEmpty) None
    else if (previous.isEmpty)
      HtmlDiff.from(next.map(HtmlDiff.AddAttribute.apply))
    else if (next.isEmpty)
      HtmlDiff.from(
        previous.map(attribute => HtmlDiff.RemoveAttribute(attribute.key))
      )
    else compare(previous, next)

  def children(
      previous: Children[Html[Event]],
      next: Children[Html[Event]]
  ): Option[HtmlDiff[Event]] =
    if (previous.isEmpty && next.isEmpty) None
    else if (previous.isEmpty)
      HtmlDiff.from(next.toList.map(HtmlDiff.AddChild.apply[Event] _ tupled))
    else if (next.isEmpty)
      HtmlDiff.from(previous.keys.map(HtmlDiff.RemoveChild))
    else compare(previous, next)

  def compare(
      previous: Attributes[Event],
      next: Attributes[Event]
  ): Option[HtmlDiff[Event]] = {
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

  def compare(
      previous: Children[Html[Event]],
      next: Children[Html[Event]]
  ): Option[HtmlDiff[Event]] = {
    val diffs = (previous zipAll next).mapFilter {
      case (key, Ior.Both(previous, next)) =>
        diff(previous, next).map(HtmlDiff.UpdateChild[Event](key, _))
      case (key, Ior.Left(_))     => HtmlDiff.RemoveChild(key).some
      case (key, Ior.Right(next)) => HtmlDiff.AddChild(key, next).some
    }

    HtmlDiff.from(diffs)
  }
}

object HtmlDiffer {
  def apply[A]: Differ[Html[A], HtmlDiff[A]] = new HtmlDiffer[A]
}

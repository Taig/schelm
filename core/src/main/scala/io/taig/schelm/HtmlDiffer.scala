package io.taig.schelm

import cats.data.Ior
import cats.implicits._

class HtmlDiffer[A] extends Differ[Html[A], HtmlDiff[A]] {
  override def diff(previous: Html[A], next: Html[A]): Option[HtmlDiff[A]] =
    // format: off
    (previous.value, next.value) match {
      case (previous: Component.Element[Html[A], A], next: Component.Element[Html[A], A]) =>
        element(previous, next)
      case (previous: Component.Fragment[Html[A]], next: Component.Fragment[Html[A]]) =>
        fragment(previous, next)
      case (previous: Component.Lazy[Html[A]], next: Component.Lazy[Html[A]]) =>
        lzy(previous, next)
      case (previous: Component.Text, next: Component.Text) =>
        text(previous, next)
      case _ => HtmlDiff.Replace(next).some
    }
    // format: on

  def element(
      previous: Component.Element[Html[A], A],
      next: Component.Element[Html[A], A]
  ): Option[HtmlDiff[A]] = {
    if (previous.name != next.name) HtmlDiff.Replace(Html(next)).some
    else {
      val diffs = listeners(previous.listeners, next.listeners).toList ++
        children(previous.children, next.children).toList
      HtmlDiff.from(diffs)
    }
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
    if (previous.hash != next.hash) diff(previous.eval.value, next.eval.value)
    else None

  def text(
      previous: Component.Text,
      next: Component.Text
  ): Option[HtmlDiff[A]] = {
    if (previous.value != next.value) HtmlDiff.UpdateText(next.value).some
    else None
  }

  def children(
      previous: Children[Html[A]],
      next: Children[Html[A]]
  ): Option[HtmlDiff[A]] =
    if (previous.isEmpty && next.isEmpty) None
    else if (previous.isEmpty)
      HtmlDiff.from(next.toList.map(HtmlDiff.addChild))
    else if (next.isEmpty)
      HtmlDiff.from(previous.keys.map(HtmlDiff.RemoveChild))
    else
      // format: off
      (previous, next) match {
        case (previous: Children.Indexed[Html[A]], next: Children.Indexed[Html[A]]) =>
          children(previous, next)
        case (previous: Children.Identified[Html[A]], next: Children.Identified[Html[A]]) =>
          children(previous, next)
        case (_, next) =>
          HtmlDiff.from(HtmlDiff.Clear +: next.toList.map(HtmlDiff.addChild))
      }
      // format: on

  def children(
      previous: Children.Indexed[Html[A]],
      next: Children.Indexed[Html[A]]
  ): Option[HtmlDiff[A]] = {
    val left = previous.values
    val right = next.values
    val diffs = (left zip right).zipWithIndex.mapFilter {
      case ((previous, next), index) =>
        diff(previous, next).map(HtmlDiff.UpdateChild(Key.Index(index), _))
    }

    if (left.length < right.length) {
      val adds = right.zipWithIndex.drop(left.length).map {
        case (html, index) => HtmlDiff.AddChild(Key.Index(index), html)
      }
      HtmlDiff.from(diffs ++ adds)
    } else if (left.length > right.length) {
      val removes = left.indices
        .drop(right.length)
        .map(Key.Index)
        .map(HtmlDiff.RemoveChild)
      HtmlDiff.from(diffs ++ removes)
    } else HtmlDiff.from(diffs)
  }

  def children(
      previous: Children.Identified[Html[A]],
      next: Children.Identified[Html[A]]
  ): Option[HtmlDiff[A]] =
    // TODO
    None

  def listeners(
      previous: Listeners[A],
      next: Listeners[A]
  ): Option[HtmlDiff[A]] =
    if (previous.isEmpty && next.isEmpty) None
    else {
      val diffs = (previous zipAll next).mapFilter {
        case (event, Ior.Both(previous, next)) =>
          if (previous == next) None
          else HtmlDiff.UpdateListener(event, next).some
        case (event, Ior.Left(_)) => HtmlDiff.RemoveListener(event).some
        case (event, Ior.Right(next)) =>
          HtmlDiff.AddListener(Listener(event, next)).some
      }

      HtmlDiff.from(diffs)
    }
}

object HtmlDiffer {
  def apply[A]: Differ[Html[A], HtmlDiff[A]] = new HtmlDiffer[A]
}

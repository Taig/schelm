package io.taig.schelm.interpreter

import cats.implicits._
import io.taig.schelm.algebra.Differ
import io.taig.schelm.data._

final class HtmlDiffer[Event] extends Differ[Html[Event], HtmlDiff[Event]] {
  override def diff(current: Html[Event], next: Html[Event]): Option[HtmlDiff[Event]] =
    (current.component, next.component) match {
      case (current: Component.Element[Event, Html[Event]], next: Component.Element[Event, Html[Event]]) =>
        element(current, next)
      case (current: Component.Fragment[Html[Event]], next: Component.Fragment[Html[Event]]) => fragment(current, next)
      case (current: Component.Text[Event], next: Component.Text[Event])                     => text(current, next)
      case _                                                                                 => HtmlDiff.Replace(next).some
    }

  def element(
      current: Component.Element[Event, Html[Event]],
      next: Component.Element[Event, Html[Event]]
  ): Option[HtmlDiff[Event]] = {
    if (current.tag.name != next.tag.name) HtmlDiff.Replace(Html(next)).some
    else {
      val diffs = attributes(current.tag.attributes, next.tag.attributes).toList ++
        listeners(current.tag.listeners, next.tag.listeners).toList

      val types = (current.tpe, next.tpe) match {
        case (Component.Element.Type.Normal(current), Component.Element.Type.Normal(next)) => children(current, next)
        case _                                                                             => None
      }

      HtmlDiff.from(diffs ++ types)
    }
  }

  def fragment(
      current: Component.Fragment[Html[Event]],
      next: Component.Fragment[Html[Event]]
  ): Option[HtmlDiff[Event]] =
    children(current.children, next.children)

  def text(current: Component.Text[Event], next: Component.Text[Event]): Option[HtmlDiff[Event]] =
    if (current.value != next.value) HtmlDiff.UpdateText(next.value).some else none

  def attributes(current: Attributes, next: Attributes): Option[HtmlDiff[Event]] =
    if (current.isEmpty && next.isEmpty) None
    else {
      val currentKeys = current.values.keys.toList
      val nextKeys = next.values.keys.toList
      val unchangedKeys = currentKeys intersect nextKeys
      val removedKeys = currentKeys diff nextKeys
      val addedKeys = nextKeys diff currentKeys

      val diffs = unchangedKeys.mapFilter { key =>
        val value = next.values(key)
        if (current.values(key) == value) None else Some(HtmlDiff.UpdateAttribute(key, value))
      } ++ removedKeys.map(HtmlDiff.RemoveAttribute) ++
        addedKeys.map(key => HtmlDiff.AddAttribute(Attribute(key, next.values(key))))

      HtmlDiff.from(diffs)
    }

  def listeners(current: Listeners[Event], next: Listeners[Event]): Option[HtmlDiff[Event]] =
    if (current.isEmpty && next.isEmpty) None
    else {
      val currentKeys = current.values.keys.toList
      val nextKeys = next.values.keys.toList
      val unchangedKeys = currentKeys intersect nextKeys
      val removedKeys = currentKeys diff nextKeys
      val addedKeys = nextKeys diff currentKeys

      val diffs = unchangedKeys.mapFilter { name =>
        val value = next.values(name)
        if (current.values(name) == value) None else Some(HtmlDiff.UpdateListener(name, value))
      } ++ removedKeys.map(HtmlDiff.RemoveListener) ++
        addedKeys.map(name => HtmlDiff.AddListener(Listener(name, next.values(name))))

      HtmlDiff.from(diffs)
    }

  def children(current: Children[Html[Event]], next: Children[Html[Event]]): Option[HtmlDiff[Event]] =
    if (current.isEmpty && next.isEmpty) None
    else if (current.isEmpty) HtmlDiff.from(next.toList.map(HtmlDiff.AppendChild[Event]))
    else if (next.isEmpty) Some(HtmlDiff.Clear)
    else
      (current, next) match {
        case (previous: Children.Indexed[Html[Event]], next: Children.Indexed[Html[Event]]) =>
          indexedChildren(previous, next)
        case (previous: Children.Identified[Html[Event]], next: Children.Identified[Html[Event]]) =>
          identifiedChildren(previous, next)
        case (_, next) => HtmlDiff.from(HtmlDiff.Clear +: next.toList.map(HtmlDiff.AppendChild[Event]))
      }

  def indexedChildren(
      previous: Children.Indexed[Html[Event]],
      next: Children.Indexed[Html[Event]]
  ): Option[HtmlDiff[Event]] = {
    val left = previous.values
    val leftLength = left.length
    val right = next.values
    val rightLength = right.length

    val diffs = (left zip right).zipWithIndex.mapFilter {
      case ((previous, next), index) => diff(previous, next).map(HtmlDiff.UpdateChild(index, _))
    }

    if (leftLength < rightLength) {
      val adds = right.slice(leftLength, rightLength).map(HtmlDiff.AppendChild[Event])
      HtmlDiff.from(diffs ++ adds)
    } else if (left.length > rightLength) {
      val removes = (rightLength until leftLength).map(HtmlDiff.RemoveChild)
      HtmlDiff.from(diffs ++ removes)
    } else HtmlDiff.from(diffs)
  }

  def identifiedChildren(
      previous: Children.Identified[Html[Event]],
      next: Children.Identified[Html[Event]]
  ): Option[HtmlDiff[Event]] =
    throw new UnsupportedOperationException("Diffing identified children is not implemented yet")
}

object HtmlDiffer {
  def apply[Event]: Differ[Html[Event], HtmlDiff[Event]] = new HtmlDiffer[Event]
}

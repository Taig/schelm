package io.taig.schelm.interpreter

import scala.annotation.nowarn

import cats.Applicative
import cats.data.Kleisli
import cats.implicits._
import io.taig.schelm.algebra.Differ
import io.taig.schelm.data._
import io.taig.schelm.data.HtmlDiff

object HtmlDiffer {
  def apply[F[_]: Applicative]: Differ[F, Html[F], HtmlDiff[F]] = {
    def diff(current: Html[F], next: Html[F]): Option[HtmlDiff[F]] =
      (current.unfix, next.unfix) match {
        case (current: Node.Element[F, Listeners[F], Html[F]], next: Node.Element[F, Listeners[F], Html[F]]) =>
          element(current, next)
        case (current: Node.Fragment[Html[F]], next: Node.Fragment[Html[F]])         => fragment(current, next)
        case (current: Node.Text[F, Listeners[F]], next: Node.Text[F, Listeners[F]]) => text(current, next)
        case _                                                                       => HtmlDiff.Replace(next).some
      }

    def element(
        current: Node.Element[F, Listeners[F], Html[F]],
        next: Node.Element[F, Listeners[F], Html[F]]
    ): Option[HtmlDiff[F]] = {
      if (current.tag.name != next.tag.name) HtmlDiff.Replace(Html(next)).some
      else {
        val diffs = attributes(current.tag.attributes, next.tag.attributes).toList ++
          listeners(current.tag.listeners, next.tag.listeners).toList

        val types = (current.variant, next.variant) match {
          case (Node.Element.Variant.Normal(current), Node.Element.Variant.Normal(next)) => children(current, next)
          case _                                                                         => None
        }

        HtmlDiff.from(diffs ++ types)
      }
    }

    def fragment(current: Node.Fragment[Html[F]], next: Node.Fragment[Html[F]]): Option[HtmlDiff[F]] =
      children(current.children, next.children)

    def text(current: Node.Text[F, Listeners[F]], next: Node.Text[F, Listeners[F]]): Option[HtmlDiff[F]] =
      if (current.value != next.value) HtmlDiff.UpdateText(next.value).some else none

    def attributes(current: Attributes, next: Attributes): Option[HtmlDiff[F]] =
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

    def listeners(current: Listeners[F], next: Listeners[F]): Option[HtmlDiff[F]] =
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

    def children(current: Children[Html[F]], next: Children[Html[F]]): Option[HtmlDiff[F]] =
      if (current.isEmpty && next.isEmpty) None
      else if (current.isEmpty) HtmlDiff.from(next.toList.map(HtmlDiff.AppendChild[F]))
      else if (next.isEmpty) Some(HtmlDiff.Clear)
      else
        (current, next) match {
          case (previous: Children.Indexed[Html[F]], next: Children.Indexed[Html[F]]) =>
            indexedChildren(previous, next)
          case (previous: Children.Identified[Html[F]], next: Children.Identified[Html[F]]) =>
            identifiedChildren(previous, next)
          case (_, next) => HtmlDiff.from(HtmlDiff.Clear +: next.toList.map(HtmlDiff.AppendChild[F]))
        }

    def indexedChildren(previous: Children.Indexed[Html[F]], next: Children.Indexed[Html[F]]): Option[HtmlDiff[F]] = {
      val left = previous.values
      val leftLength = left.length
      val right = next.values
      val rightLength = right.length

      val diffs = (left zip right).zipWithIndex.mapFilter {
        case ((previous, next), index) => diff(previous, next).map(HtmlDiff.UpdateChild(index, _))
      }

      if (leftLength < rightLength) {
        val adds = right.slice(leftLength, rightLength).map(HtmlDiff.AppendChild[F])
        HtmlDiff.from(diffs ++ adds)
      } else if (left.length > rightLength) {
        val removes = (rightLength until leftLength).map(HtmlDiff.RemoveChild)
        HtmlDiff.from(diffs ++ removes)
      } else HtmlDiff.from(diffs)
    }

    @nowarn("msg=never used")
    def identifiedChildren(
        previous: Children.Identified[Html[F]],
        next: Children.Identified[Html[F]]
    ): Option[HtmlDiff[F]] =
      throw new UnsupportedOperationException("Diffing identified children is not implemented yet")

    Kleisli {
      case (current, next) => diff(current, next).pure[F]
    }
  }
}

//package io.taig.schelm.interpreter
//
//import cats.implicits._
//import io.taig.schelm.algebra.Differ
//import io.taig.schelm.data._
//
//object HtmlDiffer {
//  def apply: Differ[Html, HtmlDiff] = new Differ[Html, HtmlDiff] {
//    override def diff(current: Html, next: Html): Option[HtmlDiff] =
//      (current.component, next.component) match {
//        case (current: Component.Element[Html], next: Component.Element[Html]) =>
//          element(current, next)
//        case (current: Component.Fragment[Html], next: Component.Fragment[Html]) =>
//          fragment(current, next)
//        case (current: Component.Text, next: Component.Text) => text(current, next)
//        case _                                                             => HtmlDiff.Replace(next).some
//      }
//
//    def element(current: Component.Element[Html], next: Component.Element[Html]): Option[HtmlDiff] = {
//      if (current.tag.name != next.tag.name) HtmlDiff.Replace(Html(next)).some
//      else {
//        val diffs = attributes(current.tag.attributes, next.tag.attributes).toList ++
//          listeners(current.tag.listeners, next.tag.listeners).toList
//
//        val types = (current.tpe, next.tpe) match {
//          case (Component.Element.Type.Normal(current), Component.Element.Type.Normal(next)) => children(current, next)
//          case _                                                                             => None
//        }
//
//        HtmlDiff.from(diffs ++ types)
//      }
//    }
//
//    def fragment(
//        current: Component.Fragment[Html],
//        next: Component.Fragment[Html]
//    ): Option[HtmlDiff] =
//      children(current.children, next.children)
//
//    def text(current: Component.Text, next: Component.Text): Option[HtmlDiff] =
//      if (current.value != next.value) HtmlDiff.UpdateText(next.value).some else none
//
//    def attributes(current: Attributes, next: Attributes): Option[HtmlDiff] =
//      if (current.isEmpty && next.isEmpty) None
//      else {
//        val currentKeys = current.values.keys.toList
//        val nextKeys = next.values.keys.toList
//        val unchangedKeys = currentKeys intersect nextKeys
//        val removedKeys = currentKeys diff nextKeys
//        val addedKeys = nextKeys diff currentKeys
//
//        val diffs = unchangedKeys.mapFilter { key =>
//          val value = next.values(key)
//          if (current.values(key) == value) None else Some(HtmlDiff.UpdateAttribute(key, value))
//        } ++ removedKeys.map(HtmlDiff.RemoveAttribute) ++
//          addedKeys.map(key => HtmlDiff.AddAttribute(Attribute(key, next.values(key))))
//
//        HtmlDiff.from(diffs)
//      }
//
//    def listeners(current: Listeners, next: Listeners): Option[HtmlDiff] =
//      if (current.isEmpty && next.isEmpty) None
//      else {
//        val currentKeys = current.values.keys.toList
//        val nextKeys = next.values.keys.toList
//        val unchangedKeys = currentKeys intersect nextKeys
//        val removedKeys = currentKeys diff nextKeys
//        val addedKeys = nextKeys diff currentKeys
//
//        val diffs = unchangedKeys.mapFilter { name =>
//          val value = next.values(name)
//          if (current.values(name) == value) None else Some(HtmlDiff.UpdateListener(name, value))
//        } ++ removedKeys.map(HtmlDiff.RemoveListener) ++
//          addedKeys.map(name => HtmlDiff.AddListener(Listener(name, next.values(name))))
//
//        HtmlDiff.from(diffs)
//      }
//
//    def children(current: Children[Html], next: Children[Html]): Option[HtmlDiff] =
//      if (current.isEmpty && next.isEmpty) None
//      else if (current.isEmpty) HtmlDiff.from(next.toList.map(HtmlDiff.AppendChild))
//      else if (next.isEmpty) Some(HtmlDiff.Clear)
//      else
//        (current, next) match {
//          case (previous: Children.Indexed[Html], next: Children.Indexed[Html]) =>
//            indexedChildren(previous, next)
//          case (previous: Children.Identified[Html], next: Children.Identified[Html]) =>
//            identifiedChildren(previous, next)
//          case (_, next) => HtmlDiff.from(HtmlDiff.Clear +: next.toList.map(HtmlDiff.AppendChild))
//        }
//
//    def indexedChildren(
//        previous: Children.Indexed[Html],
//        next: Children.Indexed[Html]
//    ): Option[HtmlDiff] = {
//      val left = previous.values
//      val leftLength = left.length
//      val right = next.values
//      val rightLength = right.length
//
//      val diffs = (left zip right).zipWithIndex.mapFilter {
//        case ((previous, next), index) => diff(previous, next).map(HtmlDiff.UpdateChild(index, _))
//      }
//
//      if (leftLength < rightLength) {
//        val adds = right.slice(leftLength, rightLength).map(HtmlDiff.AppendChild)
//        HtmlDiff.from(diffs ++ adds)
//      } else if (left.length > rightLength) {
//        val removes = (rightLength until leftLength).map(HtmlDiff.RemoveChild)
//        HtmlDiff.from(diffs ++ removes)
//      } else HtmlDiff.from(diffs)
//    }
//
//    def identifiedChildren(
//        previous: Children.Identified[Html],
//        next: Children.Identified[Html]
//    ): Option[HtmlDiff] =
//      throw new UnsupportedOperationException("Diffing identified children is not implemented yet")
//  }
//}

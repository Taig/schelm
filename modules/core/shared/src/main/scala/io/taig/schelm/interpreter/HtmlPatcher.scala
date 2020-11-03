package io.taig.schelm.interpreter

import scala.annotation.nowarn
import cats.MonadError
import cats.data.Kleisli
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Patcher, Renderer}
import io.taig.schelm.data.Node.Element.Variant
import io.taig.schelm.data.{
  Html,
  HtmlAttachedReference,
  HtmlDiff,
  HtmlHydratedReference,
  HtmlReference,
  ListenerReferences,
  Node,
  NodeReference
}

object HtmlPatcher {
  @nowarn("msg=never used")
  def apply[F[_]: MonadError[*[_], Throwable]](
      dom: Dom[F],
      renderer: Renderer[F, Html[F], HtmlReference[F]]
  ): Patcher[F, HtmlHydratedReference[F], HtmlDiff[F]] = {
    def patch(html: HtmlHydratedReference[F], diff: HtmlDiff[F], cursor: Int): F[HtmlHydratedReference[F]] = {
      (html.reference, diff) match {
        case (_, diff: HtmlDiff.Group[F]) => diff.diffs.foldLeftM(html)(patch(_, _, cursor))
//        case (reference: NodeReference.Element[F, ListenerReferences[F], HtmlHydratedReference[F]], diff: HtmlDiff.UpdateAttribute) =>
//          val tag = reference.node.tag
//          val attributes = tag.attributes.updated(diff.key, diff.value)
//          dom
//            .setAttribute(reference.dom, diff.key.value, diff.value.value)
//            .as(
//              html.copy(reference = reference.copy(node = reference.node.copy(tag = tag.copy(attributes = attributes))))
//            )
//        //      case (NodeReference.Element(_, element), HtmlDiff.AddAttribute(attribute)) =>
//        //        dom.setAttribute(element, attribute.key.value, attribute.value.value).as(html)
//        //      case (NodeReference.Element(_, element), HtmlDiff.AppendChild(html)) =>
//        //        for {
//        //          reference <- renderer.render(html)
//        //          _ <- reference.dom.traverse_(dom.appendChild(element, _))
//        //        } yield NodeReference.Element(???, element)
//        //      case (NodeReference.Fragment(_), HtmlDiff.AppendChild(html)) => ???
//        case (reference: NodeReference.Element[F, HtmlAttachedReference[F]], diff: HtmlDiff.UpdateChild[F]) =>
//          val node = reference.node
//
//          node.variant match {
//            case Variant.Normal(children) =>
//              children.get(diff.index.toLong) match {
//                case Some(child) =>
//                  patch(child, diff.diff, cursor = 0).map { update =>
//                    html.copy(reference =
//                      reference.copy(node =
//                        node.copy(variant = Node.Element.Variant.Normal(children.updated(diff.index, update)))
//                      )
//                    )
//                  }
//                case None => fail(s"No child at index ${diff.index}")
//              }
//            case Variant.Void => fail("Can not update child on a void element")
//          }
//        case (reference: NodeReference.Fragment[HtmlAttachedReference[F]], diff: HtmlDiff.UpdateChild[F]) =>
//          val node = reference.node
//          val children = node.children
//
//          children.get(diff.index.toLong).fold(fail[HtmlAttachedReference[F]](s"No child at index ${diff.index}")) {
//            child =>
//              patch(child, diff.diff, cursor = 0).map { update =>
//                html.copy(reference = reference.copy(node = node.copy(children = children.updated(diff.index, update))))
//              }
//          }
//        case (reference: NodeReference.Text[F], diff: HtmlDiff.UpdateText) =>
//          dom
//            .data(reference.dom, diff.value)
//            .as(html.copy(reference = reference.copy(node = reference.node.copy(value = diff.value))))
//        case (reference, diff) => fail(s"Can not apply $diff to $reference")

        //      case HtmlDiff.AddListener(listener) =>
        //        for {
        //          node <- select(nodes, cursor)
        //          //_ <- dom.addEventListener(node, listener.name.value, dom.callback(listener.action))
        //        } yield ()
        //      case HtmlDiff.Clear        => select(nodes, cursor).flatMap(element).flatMap(dom.innerHtml(_, ""))
        //      case HtmlDiff.Group(diffs) => diffs.traverse_(patch(nodes, _))
        //      case HtmlDiff.Replace(html) =>
        //        for {
        //          node <- select(nodes, cursor)
        //          parent <- parent(node)
        //          parent <- element(parent)
        //          next <- renderer.render(html)
        //          _ <- next.lastOption match {
        //            case Some(last) =>
        //              dom.replaceChild(parent, node, last) *>
        //                next.init.reverse.foldLeftM(last) { (reference, node) =>
        //                  dom.insertBefore(parent, node, reference.some).as(node)
        //                }
        //            case None => F.unit
        //          }
        //        } yield ()
        //      case HtmlDiff.RemoveAttribute(key) =>
        //        for {
        //          node <- select(nodes, cursor)
        //          element <- element(node)
        //          _ <- dom.removeAttribute(element, key.value)
        //        } yield ()
        //      case HtmlDiff.RemoveChild(index) =>
        //        for {
        //          node <- select(nodes, cursor)
        //          parent <- element(node)
        //          child <- child(parent, index)
        //          _ <- dom.removeChild(parent, child)
        //        } yield ()
        //      case HtmlDiff.RemoveListener(event) =>
        //        ???
        //      case HtmlDiff.UpdateAttribute(key, value) =>
        //        for {
        //          node <- select(nodes, cursor)
        //          element <- element(node)
        //          _ <- dom.setAttribute(element, key.value, value.value)
        //        } yield ()
        //      case HtmlDiff.UpdateChild(index, diff) =>
        //        for {
        //          node <- select(nodes, cursor)
        //          element <- element(node)
        //          child <- child(element, index)
        //          _ <- patch(List(child), diff, cursor = 0)
        //        } yield ()
        //      case HtmlDiff.UpdateListener(event, action) => ???
        //      case HtmlDiff.UpdateText(value)             => select(nodes, cursor).flatMap(text).flatMap(dom.data(_, value))
      }
    }

    def error(message: String): IllegalStateException = new IllegalStateException(s"$message. DOM out of sync?")

    def fail[A](message: String): F[A] = error(message).raiseError[F, A]

    Kleisli { case (reference, diff) => patch(reference, diff, cursor = 0) }
  }

  def default[F[_]: MonadError[*[_], Throwable]](dom: Dom[F]): Patcher[F, HtmlHydratedReference[F], HtmlDiff[F]] =
    HtmlPatcher(dom, HtmlRenderer(dom))
}

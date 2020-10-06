package io.taig.schelm.interpreter

import cats.MonadError
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Patcher, Renderer}
import io.taig.schelm.data.{Html, HtmlAttachedReference, HtmlDiff, HtmlReference, Node, NodeReference}

final class HtmlPatcher[F[_]](dom: Dom[F], renderer: Renderer[F, Html[F], HtmlReference[F]])(
    implicit F: MonadError[F, Throwable]
) extends Patcher[F, HtmlAttachedReference[F], HtmlDiff[F]] {
  override def patch(html: HtmlAttachedReference[F], diff: HtmlDiff[F]): F[HtmlAttachedReference[F]] =
    patch(html, diff, cursor = 0)

  def patch(html: HtmlAttachedReference[F], diff: HtmlDiff[F], cursor: Int): F[HtmlAttachedReference[F]] = {
    (html.reference, diff) match {
      case (_, HtmlDiff.Group(diffs)) => diffs.foldLeftM(html)(patch(_, _, cursor))
//      case (NodeReference.Element(_, element), HtmlDiff.AddAttribute(attribute)) =>
//        dom.setAttribute(element, attribute.key.value, attribute.value.value).as(html)
//      case (NodeReference.Element(_, element), HtmlDiff.AppendChild(html)) =>
//        for {
//          reference <- renderer.render(html)
//          _ <- reference.dom.traverse_(dom.appendChild(element, _))
//        } yield NodeReference.Element(???, element)
//      case (NodeReference.Fragment(_), HtmlDiff.AppendChild(html)) => ???
      case (reference @ NodeReference.Element(node, _), HtmlDiff.UpdateChild(index, diff)) =>
        node match {
          case node @ Node.Element(_, Node.Element.Variant.Normal(children), _) =>
            children.get(index) match {
              case Some(child) =>
                patch(child, diff, cursor = 0).map { update =>
                  html.copy(reference =
                    reference.copy(node =
                      node.copy(variant = Node.Element.Variant.Normal(children.updated(index, update)))
                    )
                  )
                }
              case None => fail(s"No child at index $index")
            }
          case Node.Element(_, Node.Element.Variant.Void, _) => fail("Can not update child on a void element")
        }
      case (reference @ NodeReference.Element(node, element), HtmlDiff.UpdateListener(name, action)) =>
        node.tag.listeners.get(name) match {
          case Some((previous, _)) =>
            val next = dom.unsafeRun(action)
            val listeners = node.tag.listeners.updated(name, next, action)
            val update =
              html.copy(reference = reference.copy(node = node.copy(tag = node.tag.copy(listeners = listeners))))
            dom.removeEventListener(element, name.value, previous) *>
              dom.addEventListener(element, name.value, next).as(update)
          case None => fail(s"No event listener for ${name.value} registered")
        }
      case (reference @ NodeReference.Text(node, text), HtmlDiff.UpdateText(value)) =>
        dom.data(text, value).as(html.copy(reference = reference.copy(node = node.copy(value = value))))
      case (reference, diff) => fail(s"Can not apply $diff to $reference")

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

  def select(nodes: List[Dom.Node], index: Int): F[Dom.Node] =
    nodes.get(index) match {
      case Some(node) => node.pure[F]
      case None       => fail(s"No node at index $index")
    }

  def element(node: Dom.Node): F[Dom.Element] = node match {
    case node: Dom.Element => node.pure[F]
    case _                 => fail[Dom.Element]("Expected an element node")
  }

  def text(node: Dom.Node): F[Dom.Text] = node match {
    case node: Dom.Text => node.pure[F]
    case _              => fail[Dom.Text]("Expected a text node")
  }

  def parent(node: Dom.Node): F[Dom.Node] =
    dom.parentNode(node).flatMap(_.liftTo[F](error("Node does not have a parent")))

  def child(parent: Dom.Element, index: Int): F[Dom.Node] =
    dom.childAt(parent, index).flatMap(_.liftTo[F](error(s"No child at index $index")))

  def error(message: String): IllegalStateException = new IllegalStateException(s"$message. DOM out of sync?")

  def fail[A](message: String): F[A] = error(message).raiseError[F, A]
}

object HtmlPatcher {
  def apply[F[_]: MonadError[*[_], Throwable]](
      dom: Dom[F],
      renderer: Renderer[F, Html[F], HtmlReference[F]]
  ): Patcher[F, HtmlAttachedReference[F], HtmlDiff[F]] = new HtmlPatcher[F](dom, renderer)
}

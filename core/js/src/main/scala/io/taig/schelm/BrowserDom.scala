package io.taig.schelm

import cats.effect.Sync
import cats.implicits._
import io.taig.schelm.internal.EffectHelpers
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.raw.HTMLInputElement

import scala.scalajs.js

final class BrowserDom[F[_], A](send: A => Unit)(implicit F: Sync[F])
    extends Dom[F, A, dom.Node] {
  override type Element = dom.Element

  override type Text = dom.Text

  override type Notify = js.Function1[dom.Event, _]

  override def lift(listener: Listener[A]): js.Function1[dom.Event, _] =
    listener match {
      case Listener.Pure(event) =>
        e =>
          e.preventDefault()
          send(event)
      case Listener.Input(event) =>
        e =>
          val value = e.target.asInstanceOf[HTMLInputElement].value
          send(event(value))
    }

  override def element(value: dom.Node): F[Element] =
    value match {
      case element: Element => element.pure[F]
      case _                => EffectHelpers.fail[F]("Not an Element. Dom out of sync?")
    }

  override def text(value: dom.Node): F[Text] =
    value match {
      case text: Text => text.pure[F]
      case _          => EffectHelpers.fail[F]("Not an Element. Dom out of sync?")
    }

  override def addEventListener(
      node: dom.Node,
      name: String,
      listener: Notify
  ): F[Unit] =
    F.delay(node.addEventListener(name, listener))

  override def appendChild(parent: dom.Node, child: dom.Node): F[Unit] =
    F.delay(parent.appendChild(child)).void

  override def createElement(name: String): F[Element] =
    F.delay(document.createElement(name))

  override def createTextNode(value: String): F[Text] =
    F.delay(document.createTextNode(value))

  override def removeAttribute(element: Element, key: String): F[Unit] =
    F.delay(element.removeAttribute(key))

  override def setAttribute(
      element: Element,
      key: String,
      value: String
  ): F[Unit] =
    F.delay(element.setAttribute(key, value))
}

//  def clear[F[_]](node: dom.Node)(implicit F: Sync[F]): F[Unit] =
//    hasChildNodes[F](node).flatMap {
//      case true =>
//        children[F](node.childNodes).flatMap { children =>
//          children.traverse_(removeChild[F](node, _))
//        }
//      case false => F.unit
//    }
//
//  def getElementById[F[_]](
//      id: String
//  )(implicit F: Sync[F]): F[Option[dom.Element]] =
//    F.delay(Option(dom.document.getElementById(id)))
//
//  def hasChildNodes[F[_]](node: dom.Node)(implicit F: Sync[F]): F[Boolean] =
//    F.delay(node.hasChildNodes())
//
//  def head[F[_]](implicit F: Sync[F]): F[HTMLHeadElement] =
//    F.delay(document.head)
//
//  def innerHtml[F[_]](node: dom.Element, html: String)(
//      implicit F: Sync[F]
//  ): F[Unit] =
//    F.delay(node.innerHTML = html)
//
//  def removeChild[F[_]](parent: dom.Node, child: dom.Node)(
//      implicit F: Sync[F]
//  ): F[Unit] =
//    F.delay(parent.removeChild(child)).void
//
//  def removeChildren[F[_]: Sync](
//      parent: dom.Node,
//      children: List[dom.Node]
//  ): F[Unit] =
//    children.traverse_(removeChild[F](parent, _))
//
//  def children[F[_]: Sync](values: NodeList): F[List[dom.Node]] =
//    (0 until values.length).toList.traverse(child(values, _))
//
//  def child[F[_]](values: NodeList, index: Int)(
//      implicit F: Sync[F]
//  ): F[dom.Node] =
//    F.delay(values.apply(index))

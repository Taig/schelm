package io.taig.schelm

import cats.effect.Sync
import cats.implicits._
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.raw.{HTMLHeadElement, NodeList}

import scala.scalajs.js

object Dom {
  def addEventListener[F[_]](
      node: dom.Node,
      name: String,
      f: js.Function1[dom.Event, _]
  )(implicit F: Sync[F]): F[Unit] =
    F.delay(node.addEventListener(name, f))

  def append[F[_]](parent: dom.Node, child: dom.Node)(
      implicit F: Sync[F]
  ): F[Unit] =
    F.delay(parent.appendChild(child)).void

  def appendAll[F[_]: Sync](
      parent: dom.Node,
      children: List[dom.Node]
  ): F[Unit] =
    children.traverse_(append(parent, _))

  def clear[F[_]](node: dom.Node)(implicit F: Sync[F]): F[Unit] =
    hasChildNodes[F](node).flatMap {
      case true =>
        children[F](node.childNodes).flatMap { children =>
          children.traverse_(removeChild[F](node, _))
        }
      case false => F.unit
    }

  def getElementById[F[_]](
      id: String
  )(implicit F: Sync[F]): F[Option[dom.Element]] =
    F.delay(Option(dom.document.getElementById(id)))

  def hasChildNodes[F[_]](node: dom.Node)(implicit F: Sync[F]): F[Boolean] =
    F.delay(node.hasChildNodes())

  def head[F[_]](implicit F: Sync[F]): F[HTMLHeadElement] =
    F.delay(document.head)

  def innerHtml[F[_]](node: dom.Element, html: String)(
      implicit F: Sync[F]
  ): F[Unit] =
    F.delay(node.innerHTML = html)

  def removeChild[F[_]](parent: dom.Node, child: dom.Node)(
      implicit F: Sync[F]
  ): F[Unit] =
    F.delay(parent.removeChild(child)).void

  def removeChildren[F[_]: Sync](
      parent: dom.Node,
      children: List[dom.Node]
  ): F[Unit] =
    children.traverse_(removeChild[F](parent, _))

  def children[F[_]: Sync](values: NodeList): F[List[dom.Node]] =
    (0 until values.length).toList.traverse(child(values, _))

  def child[F[_]](values: NodeList, index: Int)(
      implicit F: Sync[F]
  ): F[dom.Node] =
    F.delay(values.apply(index))

  def createElement[F[_]](name: String)(implicit F: Sync[F]): F[dom.Element] =
    F.delay(document.createElement(name))

  def createTextNode[F[_]](value: String)(implicit F: Sync[F]): F[dom.Text] =
    F.delay(document.createTextNode(value))

  def removeAttribute[F[_]](element: dom.Element, key: String)(
      implicit F: Sync[F]
  ): F[Unit] = F.delay(element.removeAttribute(key))

  def clearAttributes[F[_]: Sync](element: dom.Element, key: String): F[Unit] =
    (0 until element.attributes.length).toList.traverse_ { index =>
      removeAttribute(element, element.attributes(index).name)
    }

  def setAttribute[F[_]](element: dom.Element, key: String, value: String)(
      implicit F: Sync[F]
  ): F[Unit] =
    F.delay(element.setAttribute(key, value))
}

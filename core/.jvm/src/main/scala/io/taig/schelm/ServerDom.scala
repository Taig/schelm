package io.taig.schelm

import cats.effect.Sync
import cats.implicits._
import io.taig.schelm.internal.EffectHelpers
import org.jsoup.nodes.{
  Element => JElement,
  Node => JNode,
  TextNode => JText,
  Document => JDocument
}

final class ServerDom[F[_], A](document: JDocument)(implicit F: Sync[F])
    extends Dom[F, A, JNode] {
  override type Element = JElement
  override type Text = JText
  override type Notify = Unit

  override def lift(listener: Listener[A]): Unit = ()

  override def element(value: JNode): F[Element] = value match {
    case element: Element => element.pure[F]
    case _                => EffectHelpers.fail[F]("Not an Element. Dom out of sync?")
  }

  override def text(value: JNode): F[JText] =
    value match {
      case text: Text => text.pure[F]
      case _          => EffectHelpers.fail[F]("Not a Text. Dom out of sync?")
    }

  override def addEventListener(
      node: JNode,
      name: String,
      path: Path,
      notify: Unit
  ): F[Unit] = F.unit

  override def appendChild(parent: JElement, child: JNode): F[Unit] =
    F.delay(parent.appendChild(child)).void

  override def createElement(name: String): F[Element] =
    F.delay(new JElement(name))

  override def createTextNode(value: String): F[JText] =
    F.delay(new JText(value))

  override def data(text: JText, value: String): F[Unit] =
    F.delay(text.text(value)).void

  override def getElementById(id: String): F[Option[Element]] =
    F.delay(Option(document.getElementById(id)))

  override def head: F[Element] = F.delay(document.head)

  override def innerHtml(element: JElement, value: String): F[Unit] =
    F.delay(element.wrap(value)).void

  override def removeChild(parent: JElement, child: JNode): F[Unit] =
    F.delay(child.remove())

  override def removeAttribute(element: JElement, key: String): F[Unit] =
    F.delay(element.removeAttr(key)).void

  override def removeEventListener(
      node: JNode,
      name: String,
      path: Path
  ): F[Unit] = F.unit

  override def setAttribute(
      element: JElement,
      key: String,
      value: String
  ): F[Unit] =
    F.delay(element.attr(key, value)).void
}

object ServerDom {
  def apply[F[_]: Sync, A](document: JDocument): Dom[F, A, JNode] =
    new ServerDom[F, A](document)

  def apply[F[_], A](implicit F: Sync[F]): F[Dom[F, A, JNode]] =
    F.delay(new JDocument("/")).map(ServerDom[F, A])
}

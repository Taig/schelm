package io.taig.schelm

import cats.effect.Sync
import cats.implicits._
import io.taig.schelm.internal.EffectHelpers
import org.jsoup.nodes.{
  Document => JDocument,
  Element => JElement,
  Node => JNode,
  TextNode => JText
}

import scala.jdk.CollectionConverters._

final class ServerDom[F[_], Event](document: JDocument)(implicit F: Sync[F])
    extends Dom[F, Event] {
  override type Notify = Unit

  override def lift(listener: Action[Event]): Unit = ()

  override def element(value: JNode): F[JElement] = value match {
    case element: JElement => element.pure[F]
    case _                 => EffectHelpers.fail[F]("Not an Element. Dom out of sync?")
  }

  override def text(value: JNode): F[JText] =
    value match {
      case text: JText => text.pure[F]
      case _           => EffectHelpers.fail[F]("Not a Text. Dom out of sync?")
    }

  override def addEventListener(
      node: JNode,
      event: String,
      notify: Unit,
      path: Path
  ): F[Unit] = F.unit

  override def appendChild(parent: JElement, child: JNode): F[Unit] =
    F.delay(parent.appendChild(child)).void

  override def createElement(name: String): F[JElement] =
    F.delay(new JElement(name))

  override def createElementNS(namespace: String, name: String): F[Element] =
    createElement(name)

  override def createTextNode(value: String): F[JText] =
    F.delay(new JText(value))

  override def data(text: JText, value: String): F[Unit] =
    F.delay(text.text(value)).void

  override def childAt(element: JElement, index: Int): F[Option[JNode]] =
    F.delay(
      try element.child(index).some
      catch { case _: IndexOutOfBoundsException => None }
    )

  override def children(element: JElement): F[List[JNode]] =
    F.delay(element.childNodes().asScala.toList)

  override def getAttribute(element: JElement, key: String): F[Option[String]] =
    F.delay(Some(element.attr(key)).filter(_.nonEmpty))

  override def getElementById(id: String): F[Option[JElement]] =
    F.delay(Option(document.getElementById(id)))

  override def head: F[JElement] = F.delay(document.head)

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
  def apply[F[_]: Sync, Event](document: JDocument): Dom[F, Event] =
    new ServerDom[F, Event](document)

  def apply[F[_], Event](implicit F: Sync[F]): F[Dom[F, Event]] =
    F.delay(new JDocument("/")).map(ServerDom[F, Event])
}

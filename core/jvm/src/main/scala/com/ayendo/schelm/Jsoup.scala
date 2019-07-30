package com.ayendo.schelm

import cats.effect.Sync
import cats.implicits._
import org.jsoup.nodes.{Element => JElement, Node => JNode, TextNode => JText}

object Jsoup {
  def appendChild[F[_]](parent: JElement, child: JNode)(
      implicit F: Sync[F]
  ): F[Unit] =
    F.delay(parent.appendChild(child)).void

  def appendAll[F[_]: Sync](parent: JElement, children: List[JNode]): F[Unit] =
    children.traverse_(appendChild(parent, _))

  def attr[F[_]](element: JElement, key: String, value: String)(
      implicit F: Sync[F]
  ): F[Unit] =
    F.delay(element.attr(key, value)).void

  def attr[F[_]](element: JElement, key: String, value: Boolean)(
      implicit F: Sync[F]
  ): F[Unit] =
    F.delay(element.attr(key, value)).void
}

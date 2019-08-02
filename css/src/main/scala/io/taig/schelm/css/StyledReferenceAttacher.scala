package io.taig.schelm.css

import cats.effect.Sync
import cats.implicits._
import io.taig.schelm.css.StyledReferenceAttacher.Id
import io.taig.schelm.{Attacher, Dom, Reference, ReferenceAttacher}

final class StyledReferenceAttacher[F[_]: Sync, Event, Node](
    val dom: Dom[F, Event, Node],
    attacher: Attacher[F, Node, Reference[Event, Node]]
) extends Attacher[F, Node, StyledReference[Event, Node]] {
  override def attach(
      parent: Node,
      child: StyledReference[Event, Node]
  ): F[Unit] = {
    for {
      style <- getOrCreateStyleElement
      _ <- dom.innerHtml(style, s"\n${child.stylesheet}\n")
      _ <- attacher.attach(parent, child.reference)
    } yield ()
  }

  val createStyleElement: F[dom.Element] =
    for {
      element <- dom.createElement("style")
      _ <- dom.setAttribute(element, "id", Id)
      head <- dom.head
      _ <- dom.appendChild(head, element)
    } yield element

  val getOrCreateStyleElement: F[dom.Element] =
    dom.getElementById(StyledReferenceAttacher.Id).flatMap {
      case Some(element) => element.pure[F]
      case None          => createStyleElement
    }
}

object StyledReferenceAttacher {
  val Id = "schelm-css"

  def apply[F[_]: Sync, Event, Node](dom: Dom[F, Event, Node]) =
    new StyledReferenceAttacher[F, Event, Node](
      dom,
      ReferenceAttacher[F, Event, Node](dom)
    )
}

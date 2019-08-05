//package io.taig.schelm.css
//
//import cats.effect.Sync
//import cats.implicits._
//import io.taig.schelm.css.internal.StyleHelpers
//import io.taig.schelm.{Attacher, Dom, Reference, ReferenceAttacher}
//
//final class StyledReferenceAttacher[F[_]: Sync, Event, Node](
//    val dom: Dom[F, Event, Node],
//    attacher: Attacher[F, Node, Reference[Event, Node]]
//) extends Attacher[F, Node, StyledReference[Event, Node]] {
//  override def attach(
//      parent: Node,
//      child: StyledReference[Event, Node]
//  ): F[Unit] =
//    for {
//      style <- StyleHelpers.getOrCreateStyleElement(dom)
//      _ <- dom.innerHtml(style, s"\n${child.stylesheet}\n")
//      _ <- attacher.attach(parent, child.reference)
//    } yield ()
//}
//
//object StyledReferenceAttacher {
//  def apply[F[_]: Sync, Event, Node](dom: Dom[F, Event, Node]) =
//    new StyledReferenceAttacher[F, Event, Node](
//      dom,
//      ReferenceAttacher[F, Event, Node](dom)
//    )
//}

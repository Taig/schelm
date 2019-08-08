package io.taig.schelm.css

import cats.Monad
import cats.implicits._
import io.taig.schelm.css.internal.StyleHelpers
import io.taig.schelm._

final class StyledReferenceAttacher[F[_]: Monad, Event](
    dom: Dom[F, Event],
    attacher: Attacher[F, Reference[Event]],
    globals: Stylesheet
) extends Attacher[F, StyledReference[Event]] {
  override def attach(
      container: Element,
      reference: StyledReference[Event]
  ): F[Unit] = {
    for {
      _ <- attacher.attach(container, reference.reference)
      style <- StyleHelpers.getOrCreateStyleElement(dom)
      css = (globals ++ reference.stylesheet).toString
      _ <- dom.innerHtml(style, css)
    } yield ()
  }
}

object StyledReferenceAttacher {
  def apply[F[_]: Monad, Event](
      dom: Dom[F, Event],
      globals: Stylesheet
  ): Attacher[F, StyledReference[Event]] =
    new StyledReferenceAttacher[F, Event](dom, ReferenceAttacher(dom), globals)
}

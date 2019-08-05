package io.taig.schelm.css

import io.taig.schelm._

final class StyledHtmlRenderer[F[_], Event, Node](renderer: Renderer[F, Html[Event], List[Node]]) extends Renderer[F, StyledHtml[Event], (List[Node], Stylesheet)] {
  override def render(value: StyledHtml[Event], path: Path): F[(List[Node], Stylesheet)] = {
    ???
  }
}

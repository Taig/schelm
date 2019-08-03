package io.taig.schelm.css

import io.taig.schelm.Diff

final case class StyledDiff[A](diff: Diff[A], stylesheet: Stylesheet)

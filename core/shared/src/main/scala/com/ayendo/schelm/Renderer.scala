package com.ayendo.schelm

import cats.implicits._

abstract class Renderer[F[_], A, B] {
  def render(value: Html[A]): F[B]
}

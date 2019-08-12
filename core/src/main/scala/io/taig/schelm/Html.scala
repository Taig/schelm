package io.taig.schelm

final case class Html[+A](component: Component[Html[A], A])

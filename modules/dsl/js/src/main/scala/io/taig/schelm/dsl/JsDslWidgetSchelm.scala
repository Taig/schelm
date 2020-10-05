package io.taig.schelm.dsl

import cats.effect.Resource
import io.taig.schelm.algebra.Schelm
import io.taig.schelm.dsl.data.DslWidget
import io.taig.schelm.interpreter.DomSchelm

final class JsDslWidgetSchelm[F[_], Context] extends Schelm[F, DslWidget[F, Context]] {
  override def start(app: DslWidget[F, Context]): Resource[F, Unit] = ???
}

object JsDslWidgetSchelm {
  def apply[F[_], Context]: Schelm[F, DslWidget[F, Context]] = new JsDslWidgetSchelm[F, Context]
}

package io.taig.schelm.dsl

import io.taig.schelm.algebra.Schelm
import io.taig.schelm.dsl.data.DslWidget
import io.taig.schelm.interpreter.DomSchelm

final class JsDslWidgetSchelm[F[_], Context] extends Schelm[F, DslWidget[F, Context]] {
  override def start(app: DslWidget[F, Context]): F[Unit] = {
    // DomSchelm[F, DslWidget[F, Context], _, _, _](???, ???, ???, ???).start(app)
    ???
  }
}

object JsDslWidgetSchelm {
  def apply[F[_], Context]: Schelm[F, DslWidget[F, Context]] = new JsDslWidgetSchelm[F, Context]
}

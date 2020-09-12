package io.taig.schelm.dsl.operation

import io.taig.schelm.Navigator
import io.taig.schelm.css.data.CssWidget
import io.taig.schelm.data.Attributes
import io.taig.schelm.dsl.internal.Tagged
import io.taig.schelm.dsl.internal.Tagged.@@

final class AttributesOperation[F[_], Context, Tag](css: CssWidget[F, Context]) {
  def set(attributes: Attributes): CssWidget[F, Context] @@ Tag = patch(_ => attributes)

  def patch(f: Attributes => Attributes): CssWidget[F, Context] @@ Tag = {
    import cats.implicits._
    css.widget.map()
    Tagged(Navigator[CssWidget[F, Context], CssWidget[F, Context]].attributes(css, f))
  }
}

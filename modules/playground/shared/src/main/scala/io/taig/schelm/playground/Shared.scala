package io.taig.schelm.playground

import io.taig.schelm.data._
import io.taig.schelm.dsl._

object Shared {
  final case class Theme()

  sealed abstract class Event extends Product with Serializable

  def widget(label: String): StyledWidget[Event, Nothing] =
    button
      .attributes(src := "hello", style := "color: red;")(text(label))

  def component(label: String): StyledHtml2[Event] = StyledHtml2.fromStyledWidget(widget(label), Theme())

  def html(label: String): Fix[Event] = StyledHtml2.toHtml(component(label))._1
}

package io.taig.schelm

package object dsl extends syntax.component with syntax.html {
  val component: syntax.component.type = syntax.component

  val html: syntax.html.type = syntax.html
}

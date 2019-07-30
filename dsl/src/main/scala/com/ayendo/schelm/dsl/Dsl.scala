package com.ayendo.schelm.dsl

trait Dsl[A] extends CssDsl[A] with PropertiesDsl[A] with WidgetDsl[A] {}

object Dsl {
  def apply[A]: Dsl[A] = new Dsl[A] {}
}

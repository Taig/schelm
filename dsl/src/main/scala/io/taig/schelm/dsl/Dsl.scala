package io.taig.schelm.dsl

trait Dsl[Context, Payload]
    extends PropertiesDsl
    with WidgetDsl[Context, Payload]

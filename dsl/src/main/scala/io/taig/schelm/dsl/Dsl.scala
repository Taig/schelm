package io.taig.schelm.dsl

trait Dsl[Context, Payload]
    extends AttributesDsl
    with ListenersDsl
    with WidgetDsl[Context, Payload]

package io.taig.schelm.dsl

trait Dsl[Component[+_], Property[+_]]
    extends CssDsl
    with PropertiesDsl
    with ComponentDsl[Component, Property]

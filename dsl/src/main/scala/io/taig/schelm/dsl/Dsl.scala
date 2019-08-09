package io.taig.schelm.dsl

trait Dsl[Component[+_]] extends PropertiesDsl with ComponentDsl[Component]

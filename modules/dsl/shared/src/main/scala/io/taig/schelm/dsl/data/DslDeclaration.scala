package io.taig.schelm.dsl.data

import io.taig.schelm.css.data.Declaration

final case class DslDeclaration(name: Declaration.Name, value: Option[Declaration.Value])

package io.taig.schelm.dsl.data

import io.taig.schelm.css.data.Style
import io.taig.schelm.data.{Attributes, Children}

abstract class AttributesOps[+F[+_[_], -_], +G[_], -Context] {
  def get: Attributes

  def update(f: Attributes => Attributes): F[G, Context]

  final def set(attributes: Attributes): F[G, Context] = update(_ => attributes)
}

//abstract class ChildrenOps[+F[+_, -_], +Event, -Context] {
//  def get: Children[DslWidget[Event, Context]]
//
//  def update[A >: Event, B <: Context](f: Children[DslWidget[Event, Context]] => Children[DslWidget[A, B]]): F[A, B]
//
//  final def set[A >: Event, B <: Context](children: Children[DslWidget[A, B]]): F[A, B] = update(_ => children)
//}
//
//abstract class StyleOps[+F[+_, -_], +Event, -Context] {
//  def get: Style
//
//  def update(f: Style => Style): F[Event, Context]
//
//  final def set(style: Style): F[Event, Context] = update(_ => style)
//}

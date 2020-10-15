//package io.taig.schelm.dsl.keyword
//
//import io.taig.schelm.data.Attribute
//
//trait AttributeKeyword {
//  def data(name: String): Attribute.Key = Attribute.Key(s"data-$name")
//
//  val ariaLabel: Attribute.Key = Attribute.Key("aria-label")
//  val cls: Attribute.Key = Attribute.Key("class")
//  val `class`: Attribute.Key = cls
//  val role: Attribute.Key = Attribute.Key("role")
//  val src: Attribute.Key = Attribute.Key("src")
//  val style: Attribute.Key = Attribute.Key("style")
//  val tabindex: Attribute.Key = Attribute.Key("tabindex")
//}
//
//object AttributeKeyword extends AttributeKeyword

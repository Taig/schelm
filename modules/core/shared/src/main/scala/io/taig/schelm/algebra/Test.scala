package io.taig.schelm.algebra

import cats.Id

abstract class Test {
  type Foo

  def append(value: String): Foo

  def prepend(value: Foo): Foo
}

object Test {
  val x: Test = ???

  val foo = x.append("asdf")
  x.prepend(foo)
}

abstract class Yolo[F[_], Foo] {
  def lol(foo: Foo): Foo
}

object Yolo {
  def default[F[_]](x: Test): Yolo[F, x.Foo] = new Yolo[F, x.Foo] {
    override def lol(foo: x.Foo): x.Foo = ???
  }

}

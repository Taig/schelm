package io.taig.schelm.data

import cats.implicits._
import munit.FunSuite

final class IdentifierTreeTest extends FunSuite {
  val Empty: IdentifierTree[Int] = IdentifierTree.empty[Int]

  val DeepTree: IdentifierTree[Int] = IdentifierTree.of(
    value = 0,
    Identifier("foo") -> Empty,
    Identifier("bar") -> IdentifierTree.of(
      value = 1,
      Identifier("foobar") -> IdentifierTree.leaf(value = 3)
    ),
    Identifier("foobar") -> IdentifierTree.leaf(value = 5)
  )

  test("find on empty tree") {
    assertEquals(obtained = Empty.find(Identification.Root), expected = Some(Empty))
    assertEquals(obtained = Empty.find(Identification.Root / Identifier("foo")), expected = None)
  }

  test("get on deep tree") {
    assertEquals(obtained = DeepTree.find(Identification.Root).map(_.value), expected = Some(0))

    assertEquals(obtained = DeepTree.find(Identification.Root / Identifier("foo")), expected = Some(Empty))

    assertEquals(obtained = DeepTree.find(Identification.Root / Identifier("bar")).map(_.value), expected = Some(1))
    assertEquals(
      obtained = DeepTree.find(Identification.Root / Identifier("bar") / Identifier("foobar")).map(_.value),
      expected = Some(3)
    )
    assertEquals(obtained = DeepTree.find(Identification.Root / Identifier("bar") / Identifier("bar")), expected = None)

    assertEquals(obtained = DeepTree.find(Identification.Root / Identifier("foobar")).map(_.value), expected = Some(5))
  }

  test("delete on empty tree") {
    assertEquals(obtained = Empty.delete(Identification.Root), expected = Empty)
  }

  test("delete on deep tree") {
    val tree = IdentifierTree.of(
      value = 0,
      Identifier("foo") -> Empty,
      Identifier("bar") -> IdentifierTree.leaf(value = 1),
      Identifier("foobar") -> IdentifierTree.leaf(value = 5)
    )

    assertEquals(obtained = DeepTree.delete(Identification.Root), Empty)
    assertEquals(
      obtained = DeepTree.delete(Identification.Root / Identifier("bar") / Identifier("foobar")),
      expected = tree
    )
  }
}

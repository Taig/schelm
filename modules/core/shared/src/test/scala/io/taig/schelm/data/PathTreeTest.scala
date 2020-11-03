package io.taig.schelm.data

import cats.implicits._
import munit.FunSuite

final class PathTreeTest extends FunSuite {
  val Empty: PathTree[Int] = PathTree.empty[Int]

  val DeepTree: PathTree[Int] = PathTree[Int](
    value = 0,
    children = Map(
      Key.Index(0) -> Empty,
      Key.Index(1) -> PathTree(
        value = 1,
        children = Map(
          Key.Index(0) -> PathTree(value = 3, children = Map.empty)
        )
      ),
      Key.Identifier("foobar") -> PathTree(value = 5, children = Map.empty)
    )
  )

  test("find on empty tree") {
    assertEquals(obtained = Empty.find(Path.Root), expected = Some(Empty))
    assertEquals(obtained = Empty.find(Path.Root / Key.Index(0)), expected = None)
  }

  test("get on deep tree") {
    assertEquals(obtained = DeepTree.find(Path.Root / Key.Index(0)), expected = Some(Empty))

    assertEquals(obtained = DeepTree.find(Path.Root / Key.Index(1)).map(_.value), expected = Some(1))
    assertEquals(obtained = DeepTree.find(Path.Root / Key.Index(1) / Key.Index(0)).map(_.value), expected = Some(3))
    assertEquals(obtained = DeepTree.find(Path.Root / Key.Index(1) / Key.Index(1)), expected = None)
    assertEquals(obtained = DeepTree.find(Path.Root / Key.Index(1) / Key.Identifier("foobar")), expected = None)

    assertEquals(obtained = DeepTree.find(Path.Root / Key.Identifier("foobar")).map(_.value), expected = Some(5))
  }

  test("delete on empty tree") {
    assertEquals(obtained = Empty.delete(Path.Root), expected = Empty)
  }

  test("delete on deep tree") {
    val tree = PathTree(
      value = 0,
      children = Map(
        Key.Index(0) -> Empty,
        Key.Index(1) -> PathTree(
          value = 1,
          children = Map(
            Key.Index(0) -> PathTree(value = 2, children = Map.empty)
          )
        ),
        Key.Identifier("foobar") -> PathTree(value = 3, children = Map.empty)
      )
    )

    assertEquals(obtained = tree.delete(Path.Root), Empty)
    assertEquals(
      obtained = tree.delete(Path.Root / Key.Index(1)).delete(Path.Root / Key.Identifier("foobar")),
      expected = PathTree[Int](value = 0, children = Map(Key.Index(0) -> Empty))
    )
  }
}

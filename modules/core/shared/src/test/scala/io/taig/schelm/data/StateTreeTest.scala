package io.taig.schelm.data

import munit.FunSuite

final class StateTreeTest extends FunSuite {
  val DeepTree: StateTree[Int] = StateTree[Int](
    values = Vector.empty,
    children = Map(
      Key.Index(0) -> StateTree.Empty,
      Key.Index(1) -> StateTree(
        values = Vector(1),
        children = Map(
          Key.Index(0) -> StateTree(values = Vector(1, 2, 3), children = Map.empty)
        )
      ),
      Key.Identifier("foobar") -> StateTree(values = Vector(1, 2, 3), children = Map.empty)
    )
  )

  test("find on empty tree") {
    assertEquals(obtained = StateTree.Empty.find(Path.Root, state = 0), expected = None)
    assertEquals(obtained = StateTree.Empty.find(Path.Root / Key.Index(0), state = 1), expected = None)
  }

  test("find on deep tree") {
    assertEquals(obtained = DeepTree.find(Path.Root / Key.Index(0), state = 1), expected = None)

    assertEquals(obtained = DeepTree.find(Path.Root / Key.Index(1), state = 0), expected = Some(1))
    assertEquals(obtained = DeepTree.find(Path.Root / Key.Index(1), state = 1), expected = None)

    assertEquals(obtained = DeepTree.find(Path.Root / Key.Index(1) / Key.Index(0), state = 0), expected = Some(1))
    assertEquals(obtained = DeepTree.find(Path.Root / Key.Index(1) / Key.Index(0), state = 2), expected = Some(3))
    assertEquals(obtained = DeepTree.find(Path.Root / Key.Index(1) / Key.Index(0), state = 3), expected = None)

    assertEquals(obtained = DeepTree.find(Path.Root / Key.Index(1) / Key.Index(1), state = 0), expected = None)

    assertEquals(obtained = DeepTree.find(Path.Root / Key.Identifier("foobar"), state = 0), expected = Some(1))
  }

  test("delete on empty tree") {
    assertEquals(obtained = StateTree.Empty.delete(Path.Root), expected = StateTree.Empty)
  }

  test("delete on deep tree") {
    val tree = StateTree(
      values = Vector.empty[Int],
      children = Map(
        Key.Index(0) -> StateTree.Empty,
        Key.Index(1) -> StateTree(
          values = Vector(1),
          children = Map(
            Key.Index(0) -> StateTree(values = Vector(1, 2, 3), children = Map.empty)
          )
        ),
        Key.Identifier("foobar") -> StateTree(values = Vector(1, 2, 3), children = Map.empty)
      )
    )

    assertEquals(obtained = tree.delete(Path.Root), StateTree.Empty)
    assertEquals(
      obtained = tree.delete(Path.Root / Key.Index(1)).delete(Path.Root / Key.Identifier("foobar")),
      expected = StateTree[Int](values = Vector.empty, children = Map(Key.Index(0) -> StateTree.Empty))
    )
  }
}

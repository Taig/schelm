package io.taig.schelm.data

import cats.kernel.laws.discipline.{EqTests, MonoidTests}
import io.taig.schelm.util.{Cogens, Generators}
import munit.DisciplineSuite
import org.scalacheck.{Arbitrary, Cogen, Gen}

final class PathTreeLawsTest extends DisciplineSuite {
  implicit val arbitrary: Arbitrary[StateTree[Int]] = Arbitrary(Generators.pathTree(Gen.posNum[Int], maxDepth = 10))

  implicit val cogen: Cogen[StateTree[Int]] = Cogens.pathTree(Cogen[Int])

  checkAll("PathTree", MonoidTests[StateTree[Int]].monoid)
  checkAll("PathTree", EqTests[StateTree[Int]].eqv)
}

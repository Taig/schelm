package io.taig.schelm.data

import cats.kernel.laws.discipline.{EqTests, MonoidTests}
import io.taig.schelm.util.{Cogens, Generators}
import munit.DisciplineSuite
import org.scalacheck.{Arbitrary, Cogen, Gen}

final class PathTreeLawsTest extends DisciplineSuite {
  implicit val arbitrary: Arbitrary[PathTree[Int]] = Arbitrary(Generators.pathTree(Gen.posNum[Int], maxDepth = 10))

  implicit val cogen: Cogen[PathTree[Int]] = Cogens.pathTree(Cogen[Int])

  checkAll("PathTree", MonoidTests[PathTree[Int]].monoid)
  checkAll("PathTree", EqTests[PathTree[Int]].eqv)
}

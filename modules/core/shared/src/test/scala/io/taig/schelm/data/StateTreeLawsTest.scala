package io.taig.schelm.data

import cats.kernel.laws.discipline.EqTests
import cats.laws.discipline.MonoidKTests
import io.taig.schelm.util.{Cogens, Generators}
import munit.DisciplineSuite
import org.scalacheck.{Arbitrary, Cogen, Gen}

final class StateTreeLawsTest extends DisciplineSuite {
  implicit val arbitrary: Arbitrary[StateTree[Int]] = Arbitrary(Generators.stateTree(Gen.posNum[Int], maxDepth = 10))

  implicit val cogen: Cogen[StateTree[Int]] = Cogens.stateTree(Cogen[Int])

  checkAll("StateTree", MonoidKTests[StateTree].monoidK[Int])
  checkAll("StateTree", EqTests[StateTree[Int]].eqv)
}

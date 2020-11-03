package io.taig.schelm.data

import cats.kernel.laws.discipline.EqTests
import cats.laws.discipline.MonoidKTests
import io.taig.schelm.util.{Cogens, Generators}
import munit.DisciplineSuite
import org.scalacheck.{Arbitrary, Cogen, Gen}

final class StateTreeStatesLawsTest extends DisciplineSuite {
  implicit val arbitrary: Arbitrary[StateTree.States[Int]] = Arbitrary(
    Generators.stateTreeStates(Gen.posNum[Int], maxLength = 10)
  )

  implicit val cogen: Cogen[StateTree.States[Int]] = Cogens.stateTreeStates(Cogen[Int])

  checkAll("StateTree.States", MonoidKTests[StateTree.States].monoidK[Int])
  checkAll("StateTree.States", EqTests[StateTree.States[Int]].eqv)
}

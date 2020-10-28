package io.taig.schelm.data

import cats.kernel.laws.discipline.OrderTests
import io.taig.schelm.util.{Cogens, Generators}
import munit.DisciplineSuite
import org.scalacheck.{Arbitrary, Cogen}

final class KeyLawsTest extends DisciplineSuite {
  implicit val arbitrary: Arbitrary[Key] = Arbitrary(Generators.key)

  implicit val cogen: Cogen[Key] = Cogens.key

  checkAll("Key", OrderTests[Key].order)
}

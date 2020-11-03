package io.taig.schelm.data

import cats.kernel.laws.discipline.MonoidTests
import cats.{Eq, Id}
import io.taig.schelm.util.Generators
import munit.DisciplineSuite
import org.scalacheck.{Arbitrary, Gen}

final class ListenersLawsTest extends DisciplineSuite {
  implicit val arbitrary: Arbitrary[Listeners[Id]] = Arbitrary(
    Generators.listeners(Gen.const[Listener.Action[Id]](_ => ()))
  )

  implicit val eq: Eq[Listeners[Id]] = Eq.by(_.values.keySet.map(_.value))

  checkAll("Listeners", MonoidTests[Listeners[Id]].monoid)
}

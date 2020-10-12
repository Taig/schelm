package io.taig.schelm.documentation

import cats.effect.{ExitCode, IO, IOApp, Resource}
import cats.implicits._
import io.taig.schelm.css.data.Style
import io.taig.schelm.dsl._
import io.taig.schelm.dsl.data.DslNode
import io.taig.schelm.interpreter.BrowserDom
import io.taig.schelm.mdc.{MdcTheme, MdcTopAppBar}

object Main extends IOApp {
  val app: DslNode[Nothing, Nothing, MdcTheme] = MdcTopAppBar.regular(
    "Schelm",
    style = Style.of(
      boxShadow := "0px 2px 4px -1px rgba(0,0,0,0.2), 0px 4px 5px 0px rgba(0,0,0,0.14), 0px 1px 10px 0px rgba(0,0,0,0.12)"
    )
  )

  override def run(args: List[String]): IO[ExitCode] = {
    val dom = BrowserDom[IO]

    (for {
      root <- Resource.liftF(dom.getElementById("main").flatMap(_.liftTo[IO](new IllegalStateException)))
      schelm <- Resource.liftF(Schelm.default[IO, Event, MdcTheme](dom)(root))
      _ <- schelm.start(State.Initial)(_ => MdcTheme.Default, _ => app)
    } yield ExitCode.Success).use(_ => IO.never)
  }
}

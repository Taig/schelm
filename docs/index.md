---
layout: home
title:  "Home"
section: "home"
technologies:
 - first: ["Scala", "Schelm is completely written in Scala"]
 - second: ["JavaScript", "Schelm apps are powered by Scala.js and run in the browser"]
 - third: ["Elm", "A purely functional approach to web apps"]
---

# Schelm

[![GitLab CI](https://gitlab.com/taig-github/schelm/badges/master/build.svg?style=flat-square)](https://gitlab.com/taig-github/schelm/pipelines)
[![Maven Central](https://img.shields.io/maven-central/v/io.taig/schelm_2.13.svg?style=flat-square)](https://index.scala-lang.org/taig/schelm)

> Elm is a domain-specific programming language for declaratively creating web browser-based graphical user interfaces. Elm is purely functional, and is developed with emphasis on usability, performance, and robustness. It advertises "no runtime exceptions in practice", made possible by the Elm compiler's static type checking.

— <a href="https://en.wikipedia.org/wiki/Elm_(programming_language)"><cite>https://en.wikipedia.org/wiki/Elm_(programming_language)</cite></a>

## Feature overview

- Schelm copies [Elm's](https://elm-lang.org/) design closely, the available features work identical
- An app can subscribe to an [fs2.Stream](https://github.com/functional-streams-for-scala/fs2) to receive external events
- The `css` module is inspired by [elm-css](https://github.com/rtfeldman/elm-css), but no as closely copied as the `core`
- Schelm apps can be rendered on the JVM natively (SSR)
- Some more sophisticated features such as `Html.lazy` are currently not available in Schelm but will be added later

## Getting Started

Schelm is split into 3 major modules:

1. `@MODULE_CORE@` contains all data structures and implementations necessary to create and run a website. All other modules just add convenience to this api.
2. `@MODULE_CSS@` introduces the notion of CSS. Styles can be attached to components that will then be moved to the `<style />` section of the document, a generated class is then added to the component in order to receive the styles. 
3. `@MODULE_DSL@` adds plenty of convenience methods to build up HTML structures, add attributes and attach listeners or styles.

All of these modules are available for the JVM (Scala @SCALA_VERSIONS@) and JS (Scala.js @SCALAJS_VERSION@).

```scala
libraryDependencies ++=
  "@ORGANIZATION@" %%% "@MODULE_CORE@" % "@VERSION@" ::
  "@ORGANIZATION@" %%% "@MODULE_CSS@" % "@VERSION@" ::
  "@ORGANIZATION@" %%% "@MODULE_DSL@" % "@VERSION@" ::
  Nil
```

## Attributions

Schelm is a Scala(.js) port of [Elm](https://elm-lang.org/) and is also highly inspired by the very similar [scalm](https://github.com/julienrf/scalm) project.

## License

Schelm is available under the MIT license, available at [https://opensource.org/licenses/MIT](https://opensource.org/licenses/MIT) and in the [license file](https://github.com/taig/schelm/blob/master/LICENSE). 
---
layout: home
title:  "Home"
section: "home"
---

# Schelm

Lorem ipsum

## Getting Started

Schelm is split into 3 major modules:

1. `@MODULE_CORE@` contains all data structures and implementations necessary to create and run a website. All other modules just add convenience to this api.
2. `@MODULE_CSS@` introduces the notion of CSS. Styles can be attached to components that will then be moved to the `<style />` section of the document, a generated class is then added to the component in order to receive the styles. 
3. `@MODULE_DSL@` adds plenty of convenience methods to build up HTML structures, add attributes and attach listeners or styles.

All of these modules are available for the JVM and JS.

```scala
libraryDependencies ++=
  "@ORGANIZATION@" %%% "@MODULE_CORE@" % "@VERSION@" ::
  "@ORGANIZATION@" %%% "@MODULE_CSS@" % "@VERSION@" ::
  "@ORGANIZATION@" %%% "@MODULE_DSL@" % "@VERSION@" ::
  Nil
```
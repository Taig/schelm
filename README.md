# schelm

> The Elm architecture on top of cats-effect and fs2

[![GitLab CI](https://gitlab.com/taig-github/schelm/badges/master/build.svg?style=flat-square)](https://gitlab.com/taig-github/schelm/pipelines)
[![Maven Central](https://img.shields.io/maven-central/v/io.taig/schelm_2.13.svg?style=flat-square)](https://index.scala-lang.org/taig/schelm)

## Work in progress

- [x] Basic rendering, diffing and patching support
- [x] Rendering on JVM (SSR)
- [x] CSS support
- [x] Fully featured DSL for HTML, attributes, listeners and CSS
- [x] Support for Html.lazy
- [ ] Keyed children diffing
- [ ] Dom parsing (e.g. for SSR continuation)
- [ ] Ports to interact with JavaScript
- [ ] Documentation & project website
- [ ] Test suite

## Documentation

Please visit the [microsite](http://taig.io/schelm/).

## Building the microsite

The microsite relies on [`sbt-microsites`](https://github.com/47deg/sbt-microsites) and does therefore require `ruby` and `jekyll` to be installed on your system. When these requirements are met, the microsite can be built as follows.

```
sbt website/makeMicrosite
cd website/target/site/
jekyll serve
```

Alternatively, when `ruby` and `jekyll` are not available the microsite can be built via docker.

```
docker build -t schelm .
docker run -it -p 4000:4000 -v $PWD:/home/schelm/ schelm 
sbt website/makeMicrosite
cd website/target/site/
jekyll serve --host=0.0.0.0
```
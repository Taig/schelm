# schelm

> The Elm architecture on top of cats-effect and fs2

[![GitLab CI](https://gitlab.com/taig-github/schelm/badges/master/build.svg?style=flat-square)](https://gitlab.com/taig-github/schelm/pipelines)
[![Maven Central](https://img.shields.io/maven-central/v/io.taig/schelm_2.13.svg?style=flat-square)](https://index.scala-lang.org/taig/schelm)

## Work in progress

- [x] Basic rendering, diffing and patching support
- [x] Rendering on JVM (SSR)
- [x] CSS support
- [x] Fully featured DSL for HTML, attributes, listeners and CSS
- [ ] Documentation & project website
- [ ] Dom parsing (e.g. for SSR continuation)
- [ ] Test suite

## Build website

Steps to build the documentation locally, but within a docker container.

  1. Build the container if not already done
     ```
     docker build -t schelm .
     ```

  2. Run the container, exposing port 4000 for Jekyll
     ```
     docker run -it -v $PWD:/home/schelm/ -p 4000:4000 schelm
     ```

  3. Now inside the container, generate the microsite
     ```
     sbt docs
     ```

  4. Start the Jekyll server
     ```
     cd website/target/site
     jekyll serve --host 0.0.0.0
     ```

  5. Navigate to `localhost:4000` in your browser.
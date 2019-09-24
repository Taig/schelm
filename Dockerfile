FROM        openjdk:8u212-jdk-alpine3.9

RUN         apk upgrade --update
RUN         apk add --no-cache bash build-base git nodejs ruby-full ruby-dev
RUN         gem install --no-document jekyll

# Install sbt
RUN         curl -Ls https://git.io/sbt > /usr/local/bin/sbt
RUN         chmod 0755 /usr/local/bin/sbt

# Cache sbt
RUN         mkdir -p \
              ./cache/project/ \
              ./cache/src/main/scala/
ADD         ./.jvmopts ./cache/.jvmopts
ADD         ./project/build.properties ./cache/project/
RUN         cd ./cache/ && sbt -v exit

# Cache scala
ADD         ./scalaVersion.sbt ./cache/
RUN         echo "class App" > ./cache/src/main/scala/App.scala
RUN         cd ./cache/ && sbt -v +compile

# Cache plugins
ADD         ./project/plugins.sbt ./cache/project/
RUN         cd ./cache/ && sbt -v +compile

# Cache dependencies
ADD         ./project ./cache/project/
ADD         ./build.sbt ./cache/
RUN         mkdir ./cache/docs/
RUN         touch ./cache/docs/index.md
RUN         cd ./cache/ && sbt -v ";set every sourceGenerators := List.empty;+compile;website/makeMicrosite"

# Clean cache
RUN         rm -r ./cache/

WORKDIR     /home/schelm/

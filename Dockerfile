FROM        openjdk:8u222-jdk-slim-buster

RUN         apt-get update
RUN         apt-get install --yes curl

# Install sbt
RUN         curl -Ls https://git.io/sbt > /usr/local/bin/sbt
RUN         chmod 0755 /usr/local/bin/sbt

# Cache sbt
RUN         mkdir -p \
              ./cache/project/ \
              ./cache/src/main/scala/
ADD         ./project/build.properties ./cache/project/
RUN         cd ./cache/ && sbt -v exit

# Cache scala
ADD         ./scalaVersion.sbt ./cache/
RUN         echo "class App" > ./cache/src/main/scala/App.scala
RUN         cd ./cache/ && sbt -v compile

# Cache plugins
ADD         ./project/plugins.sbt ./cache/project/
RUN         cd ./cache/ && sbt -v compile

# Cache dependencies
ADD         ./project ./cache/project/
ADD         ./build.sbt ./cache/
RUN         cd ./cache/ && sbt -v ";set every sourceGenerators := List.empty;+test:compile"

# Clean cache
RUN         rm -r ./cache/

WORKDIR     /home/schelm/

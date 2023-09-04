#!/usr/bin/env bash
set -e

./gradlew build -x test
docker build -t boot3-java  ./ -f Dockerfile-java
docker build -t boot3-native ./
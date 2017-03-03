#!/usr/bin/env bash
./gradlew :hazelcast-server:clean
./gradlew :hazelcast-server:shadowJar && java -jar hazelcast-server/build/libs/ipa-hazelcast-server-0.1.0-all.jar